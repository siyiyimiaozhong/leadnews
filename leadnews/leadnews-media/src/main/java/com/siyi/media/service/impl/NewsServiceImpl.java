package com.siyi.media.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siyi.common.kafka.messages.SubmitArticleAuthMessage;
import com.siyi.common.media.constans.WmMediaConstans;
import com.siyi.media.kafka.AdminMessageSender;
import com.siyi.media.service.NewsService;
import com.siyi.model.common.dtos.PageResponseResult;
import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.common.enums.AppHttpCodeEnum;
import com.siyi.model.mappers.app.ApArticleConfigMapper;
import com.siyi.model.mappers.wemedia.WmMaterialMapper;
import com.siyi.model.mappers.wemedia.WmNewsMapper;
import com.siyi.model.mappers.wemedia.WmNewsMaterialMapper;
import com.siyi.model.media.dtos.WmNewsDto;
import com.siyi.model.media.dtos.WmNewsPageReqDto;
import com.siyi.model.media.pojos.WmMaterial;
import com.siyi.model.media.pojos.WmNews;
import com.siyi.model.media.pojos.WmUser;
import com.siyi.model.mess.admin.SubmitArticleAuto;
import com.siyi.utils.threadlocal.WmThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@SuppressWarnings("all")
public class NewsServiceImpl implements NewsService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WmMaterialMapper wmMaterialMapper;
    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private AdminMessageSender adminMessageSender;

    @Value("${FILE_SERVER_URL}")
    private String fileServerUrl;

    @Override
    public ResponseResult saveNews(WmNewsDto dto, Short type) {
        if (dto == null || !StringUtils.isNotEmpty(dto.getContent())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmUser user = WmThreadLocalUtils.getUser();
        //如果是修改先删除所有素材关联关系
        if (dto.getId() != null) {
            wmNewsMaterialMapper.delByNewsId(dto.getId());
        }

        //解析文章类容，进行图文素材关联
        String content = dto.getContent();
        //Map<图片排序号， dfs文件id>
        Map<String, Object> materials;
        try {
            List<Map> list = objectMapper.readValue(content, List.class);
            //抽取信息
            Map<String, Object> extractInfo = extractUrlInfo(list);
            materials = (Map<String, Object>) extractInfo.get("materials");
            //文章图片总数量
            int countImageNum = (int) extractInfo.get("countImageNum");
            //保存发布文章信息
            WmNews wmNews = new WmNews();
            BeanUtils.copyProperties(dto, wmNews);
            if (dto.getType().equals(WmMediaConstans.WM_NEWS_TYPE_AUTO)) {
                saveWmNews(wmNews, countImageNum, type);
            } else {
                saveWmNews(wmNews, dto.getType(), type);
            }
            //保存内容中的图片和当前文章的关系
            if (materials.keySet().size() != 0) {
                ResponseResult responseResult = saveRelativeInfoForContent(materials, wmNews.getId());
                if (responseResult != null) {
                    return responseResult;
                }
            }
            //封面图片关联
            ResponseResult responseResult = coverImagesRelation(dto, materials, wmNews, countImageNum);
            if (responseResult != null) {
                return responseResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("parse content error, param content :{}", content);
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult listByUser(WmNewsPageReqDto dto) {
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //检测参数
        dto.checkParam();
        Long uid = WmThreadLocalUtils.getUser().getId();
        List<WmNews> datas = wmNewsMapper.selectBySelective(dto, uid);
        int total = wmNewsMapper.countSelectBySelective(dto, uid);
        PageResponseResult responseResult = new PageResponseResult(dto.getPage(),
                dto.getSize(), total);
        responseResult.setData(datas);
        responseResult.setHost(fileServerUrl);
        return responseResult;
    }

    @Override
    public ResponseResult findWmNewsById(WmNewsDto dto) {
        if (dto == null || dto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "文章ID不可缺少");
        }
        WmNews wmNews = wmNewsMapper.selectNewsDetailByPrimaryKey(dto.getId());
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "文章不存在");
        }
        ResponseResult responseResult = ResponseResult.okResult(wmNews);
        responseResult.setHost(fileServerUrl);
        return responseResult;
    }

    @Override
    public ResponseResult delNews(WmNewsDto dto) {
        if (dto == null || dto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews wmNews = wmNewsMapper.selectNewsDetailByPrimaryKey(dto.getId());
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "文章不存在");
        }
        //判断是否审核通过
        if (WmMediaConstans.WM_NEWS_AUTHED_STATUS.equals(wmNews.getStatus()) ||
                WmMediaConstans.WM_NEWS_PUBLISH_STATUS.equals(wmNews.getStatus())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "当前文章已通过审核不可删除");
        }
        //删除文章素材关联表信息
        wmNewsMaterialMapper.delByNewsId(wmNews.getId());
        //删除文章信息
        wmNewsMapper.deleteByPrimaryKey(wmNews.getId());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 封面图片关联
     *
     * @param dto
     * @param materials
     * @param wmNews
     * @param countImageNum
     * @return
     */
    private ResponseResult coverImagesRelation(WmNewsDto dto, Map<String, Object> materials, WmNews wmNews, int countImageNum) {
        List<String> images = dto.getImages();
        if (!WmMediaConstans.WM_NEWS_TYPE_AUTO.equals(dto.getType()) && dto.getType() != images.size()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "图文模式不匹配");
        }
        //如果是自动匹配封面
        if (WmMediaConstans.WM_NEWS_TYPE_AUTO.equals(dto.getType())) {
            images = new ArrayList<>();
            if (countImageNum < WmMediaConstans.WM_NEWS_MANY_IMAGE) {
                for (Object value : materials.values()) {
                    images.add(String.valueOf(value));
                    break;
                }
            }
            if (countImageNum >= WmMediaConstans.WM_NEWS_MANY_IMAGE) {
                for (int i = 0; i < WmMediaConstans.WM_NEWS_MANY_IMAGE; i++) {
                    images.add((String) materials.get(String.valueOf(i)));
                }
            }
            if (images.size() != 0) {
                ResponseResult responseResult = saveRelativeInfoForCover(images, wmNews.getId());
                if (responseResult != null) {
                    return responseResult;
                }
            }
        } else if (images != null && images.size() != 0) {
            ResponseResult responseResult = saveRelativeInfoForCover(images, wmNews.getId());
            if (responseResult != null) {
                return responseResult;
            }
        }
        //更新images字段
        if (images != null) {
            wmNews.setImages(
                    StringUtils.join(
                            images.stream().map(s -> s.replace(fileServerUrl, "")).collect(Collectors.toList()),
                            WmMediaConstans.WM_NEWS_IMAGES_SWPARATOR
                    )
            );
            wmNewsMapper.updateByPrimaryKey(wmNews);
        }
        return null;
    }

    /**
     * 提取图片信息
     *
     * @param list
     * @return
     */
    private Map<String, Object> extractUrlInfo(List<Map> list) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> materials = new HashMap<>();
        int order = 0;
        int countImageNum = 0;
        //收集文章中引用的资源服务器的图片url以及排序
        for (Map map : list) {
            order++;
            if (WmMediaConstans.WM_NEWS_TYPE_IMAGE.equals(map.get("type"))) {
                countImageNum++;
                String imgUrl = String.valueOf(map.get("value"));
                if (imgUrl.startsWith(fileServerUrl)) {
                    materials.put(String.valueOf(order), imgUrl.replace(fileServerUrl, ""));
                }
            }
        }
        res.put("materials", materials);
        res.put("countImageNum", countImageNum);
        return res;
    }

    /**
     * 保存关联信息到数据库
     *
     * @param materials
     * @param newsId
     */
    private ResponseResult saveRelativeInfo(Map<String, Object> materials, Integer newsId, Short type) {
        WmUser user = WmThreadLocalUtils.getUser();
        //手机数据库中的素材信息
        List<WmMaterial> dbMaterialInfos = wmMaterialMapper.findMaterialByUidAndImgUrls(user.getId(), materials.values());
        if (dbMaterialInfos != null && dbMaterialInfos.size() != 0) {
            Map<String, Object> urlIdMap = dbMaterialInfos.stream().collect(Collectors.toMap(WmMaterial::getUrl, WmMaterial::getId));
            for (String key : materials.keySet()) {
                String fileId = String.valueOf(urlIdMap.get(materials.get(key)));
                if ("null".equals(fileId)) {
                    return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "应用图片失效");
                }
                materials.put(key, String.valueOf(fileId));
            }
            //存储关系数据到数据库
            wmNewsMaterialMapper.saveRelationsByContent(materials, newsId, type);
        }
        return null;
    }


    /**
     * 保存图片关系为封面
     *
     * @param images
     * @param newsId
     */
    private ResponseResult saveRelativeInfoForCover(List<String> images, Integer newsId) {
        Map<String, Object> materials = new HashMap<>();
        for (int i = 0; i < images.size(); i++) {
            String s = images.get(i);
            s = s.replace(fileServerUrl, "");
            materials.put(String.valueOf(i), s);
        }
        return saveRelativeInfo(materials, newsId, WmMediaConstans.WM_IMAGE_REFERENCE);
    }


    /**
     * 保存图片关系为内容
     *
     * @param materials
     * @param newsId
     */
    private ResponseResult saveRelativeInfoForContent(Map<String, Object> materials, Integer newsId) {
        return saveRelativeInfo(materials, newsId, WmMediaConstans.WM_CONTENT_REFERENCE);
    }

    /**
     * 保存/修改发布文章信息
     *
     * @param wmNews
     * @param countImageNum
     * @param type
     */
    private void saveWmNews(WmNews wmNews, int countImageNum, Short type) {
        WmUser user = WmThreadLocalUtils.getUser();
        //保存提交文章数据
        if (countImageNum == WmMediaConstans.WM_NEWS_SINGLE_IMAGE) {
            wmNews.setType(WmMediaConstans.WM_NEWS_SINGLE_IMAGE);
        } else if (countImageNum >= WmMediaConstans.WM_NEWS_MANY_IMAGE) {
            wmNews.setType(WmMediaConstans.WM_NEWS_MANY_IMAGE);
        } else {
            wmNews.setType(WmMediaConstans.WM_NEWS_NONE_IMAGE);
        }
        wmNews.setStatus(type);
        wmNews.setUserId(user.getId());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setEnable((short) 1);

        int temp = 0;
        if (wmNews.getId() == null) {
            temp = wmNewsMapper.insertNewsForEdit(wmNews);
        } else {
            temp = wmNewsMapper.updateByPrimaryKey(wmNews);
        }

        if (temp == 1 && WmMediaConstans.WM_NEWS_SUMMIT_STATUS == type) {
            SubmitArticleAuthMessage message = new SubmitArticleAuthMessage();
            SubmitArticleAuto submitArticleAuto = new SubmitArticleAuto();
            submitArticleAuto.setArticleId(wmNews.getId());
            submitArticleAuto.setType(SubmitArticleAuto.ArticleType.WEMEDIA);
            message.setData(submitArticleAuto);
            adminMessageSender.sendMessage(message);
        }
    }

}