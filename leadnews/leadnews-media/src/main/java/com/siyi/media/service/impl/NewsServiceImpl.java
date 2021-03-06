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
        //????????????????????????????????????????????????
        if (dto.getId() != null) {
            wmNewsMaterialMapper.delByNewsId(dto.getId());
        }

        //?????????????????????????????????????????????
        String content = dto.getContent();
        //Map<?????????????????? dfs??????id>
        Map<String, Object> materials;
        try {
            List<Map> list = objectMapper.readValue(content, List.class);
            //????????????
            Map<String, Object> extractInfo = extractUrlInfo(list);
            materials = (Map<String, Object>) extractInfo.get("materials");
            //?????????????????????
            int countImageNum = (int) extractInfo.get("countImageNum");
            //????????????????????????
            WmNews wmNews = new WmNews();
            BeanUtils.copyProperties(dto, wmNews);
            if (dto.getType().equals(WmMediaConstans.WM_NEWS_TYPE_AUTO)) {
                saveWmNews(wmNews, countImageNum, type);
            } else {
                saveWmNews(wmNews, dto.getType(), type);
            }
            //????????????????????????????????????????????????
            if (materials.keySet().size() != 0) {
                ResponseResult responseResult = saveRelativeInfoForContent(materials, wmNews.getId());
                if (responseResult != null) {
                    return responseResult;
                }
            }
            //??????????????????
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
        //????????????
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
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "??????ID????????????");
        }
        WmNews wmNews = wmNewsMapper.selectNewsDetailByPrimaryKey(dto.getId());
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "???????????????");
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
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "???????????????");
        }
        //????????????????????????
        if (WmMediaConstans.WM_NEWS_AUTHED_STATUS.equals(wmNews.getStatus()) ||
                WmMediaConstans.WM_NEWS_PUBLISH_STATUS.equals(wmNews.getStatus())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "???????????????????????????????????????");
        }
        //?????????????????????????????????
        wmNewsMaterialMapper.delByNewsId(wmNews.getId());
        //??????????????????
        wmNewsMapper.deleteByPrimaryKey(wmNews.getId());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * ??????????????????
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
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "?????????????????????");
        }
        //???????????????????????????
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
        //??????images??????
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
     * ??????????????????
     *
     * @param list
     * @return
     */
    private Map<String, Object> extractUrlInfo(List<Map> list) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> materials = new HashMap<>();
        int order = 0;
        int countImageNum = 0;
        //????????????????????????????????????????????????url????????????
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
     * ??????????????????????????????
     *
     * @param materials
     * @param newsId
     */
    private ResponseResult saveRelativeInfo(Map<String, Object> materials, Integer newsId, Short type) {
        WmUser user = WmThreadLocalUtils.getUser();
        //?????????????????????????????????
        List<WmMaterial> dbMaterialInfos = wmMaterialMapper.findMaterialByUidAndImgUrls(user.getId(), materials.values());
        if (dbMaterialInfos != null && dbMaterialInfos.size() != 0) {
            Map<String, Object> urlIdMap = dbMaterialInfos.stream().collect(Collectors.toMap(WmMaterial::getUrl, WmMaterial::getId));
            for (String key : materials.keySet()) {
                String fileId = String.valueOf(urlIdMap.get(materials.get(key)));
                if ("null".equals(fileId)) {
                    return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "??????????????????");
                }
                materials.put(key, String.valueOf(fileId));
            }
            //??????????????????????????????
            wmNewsMaterialMapper.saveRelationsByContent(materials, newsId, type);
        }
        return null;
    }


    /**
     * ???????????????????????????
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
     * ???????????????????????????
     *
     * @param materials
     * @param newsId
     */
    private ResponseResult saveRelativeInfoForContent(Map<String, Object> materials, Integer newsId) {
        return saveRelativeInfo(materials, newsId, WmMediaConstans.WM_CONTENT_REFERENCE);
    }

    /**
     * ??????/????????????????????????
     *
     * @param wmNews
     * @param countImageNum
     * @param type
     */
    private void saveWmNews(WmNews wmNews, int countImageNum, Short type) {
        WmUser user = WmThreadLocalUtils.getUser();
        //????????????????????????
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