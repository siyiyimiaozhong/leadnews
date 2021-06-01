package com.siyi.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.siyi.admin.service.ReviewMediaArticleService;
import com.siyi.common.aliyun.AliyunImageScanRequest;
import com.siyi.common.aliyun.AliyunTextScanRequest;
import com.siyi.common.article.constans.ESIndexConstants;
import com.siyi.common.common.pojo.EsIndexEntity;
import com.siyi.common.kafka.KafkaSender;
import com.siyi.model.admin.pojos.AdChannel;
import com.siyi.model.article.pojos.ApArticle;
import com.siyi.model.article.pojos.ApArticleConfig;
import com.siyi.model.article.pojos.ApArticleContent;
import com.siyi.model.article.pojos.ApAuthor;
import com.siyi.model.crawler.core.parse.ZipUtils;
import com.siyi.model.mappers.admin.AdChannelMapper;
import com.siyi.model.mappers.app.*;
import com.siyi.model.mappers.wemedia.WmNewsMapper;
import com.siyi.model.mappers.wemedia.WmUserMapper;
import com.siyi.model.media.pojos.WmNews;
import com.siyi.model.media.pojos.WmUser;
import com.siyi.model.mess.admin.ArticleAuditSuccess;
import com.siyi.model.user.pojos.ApUserMessage;
import com.siyi.utils.common.Compute;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
@SuppressWarnings("all")
public class ReviewMediaArticleServiceImpl implements ReviewMediaArticleService {
    @Autowired
    private WmNewsMapper wmNewsMapper;

/*    @Value("${review_article_pass}")
    private Double review_article_pass;

    @Value("${review_article_review}")
    private Double review_article_review;*/

    @Autowired
    private AliyunTextScanRequest aliyunTextScanRequest;
    @Autowired
    private AliyunImageScanRequest aliyunImageScanRequest;
    @Autowired
    private JestClient jestClient;
    //ap_article_config   ap_article   ap_article_content  ap_author
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private ApAuthorMapper apAuthorMapper;
    @Autowired
    private AdChannelMapper adChannelMapper;
    @Autowired
    private WmUserMapper wmUserMapper;
    @Autowired
    private ApUserMessageMapper apUserMessageMapper;
    @Autowired
    private KafkaSender kafkaSender;

    /**
     * 主图数量
     */
    private static final Integer MAIN_PICTURE_SIZE = 5;

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    /**
     * 自媒体文章发布审核
     *
     * @param newsId 文章id
     */
    @Override
    public void autoReviewArticleByMedia(Integer newsId) {
        //1.根据文章id查询文章内容，内容包括文本和图片
//        WmNews wmNews = wmNewsMapper.findbyNewsIdAndStatus(newsId, 1);
        WmNews wmNews = wmNewsMapper.selectNewsDetailByPrimaryKey(newsId);

        //人工审核   直接保存数据和创建索引
        if (wmNews != null && wmNews.getStatus() == 4) {
            reviewSuccessSaveAll(wmNews);
            return;
        }
        //审核通过后待发布的文章，判断发布时间
        if (wmNews != null && wmNews.getStatus() == 8 && wmNews.getPublishTime()!=null && wmNews.getPublishTime().getTime() > new Date().getTime()) {
            reviewSuccessSaveAll(wmNews);
            return;
        }

        if (wmNews != null && wmNews.getStatus() == 1) {
            //2.根据文章标题匹配文章内容 匹配度
            String content = wmNews.getContent();//文章内容
            String title = wmNews.getTitle();//文章标题
            double degree = Compute.SimilarDegree(content, title);
            if (degree <= 0) {
                //文章标题与内容不匹配
                updateWmNews(wmNews,(short)2,"文章标题与内容不匹配");
                return;
            }
            //TODO 测试阶段，暂时不对文章标题和文章内容匹配的审核
            /*double semblance = SimHashUtils.getSemblance(title, content, 64);
            if(semblance<review_article_pass && semblance>review_article_review){
                //人工审核，修改状态
                updateWmNews(wmNews, (short) 3, "人工复审");
                return;
            }
            if(semblance<review_article_review){
                //文章与标题不匹配，拒绝
                updateWmNews(wmNews, (short) 2, "文章与标题不匹配");
                //系统推送消息，审核结果
                saveApUserMessage(wmNews, 109, "审核未通过，文章内容与标题不匹配");
                return;
            }*/

            //3.调用阿里接口，审核文本
            List<String> images = new ArrayList<>();
            StringBuffer sb = new StringBuffer();
            JSONArray jsonArray = JSON.parseArray(content);
            handlerTextAndImages(images, sb, jsonArray);
            //截取之前五张图片
            if (images.size() > MAIN_PICTURE_SIZE) {
                images = images.subList(0, MAIN_PICTURE_SIZE);
            }
            //TODO 测试阶段 暂时不进行图片和文本内容的审核
            /*try {
                String response = aliyunTextScanRequest.textScanRequest(sb.toString());
                if ("review".equals(response)){//人工审核
                    updateWmNews(wmNews,(short)3, "需要人工审核");
                    return;
                } else if (!"pass".equals(response)){
                    //文章内容审核没有通过
                    updateWmNews(wmNews, (short) 2, "文章内容有违规行为");
                    return;
                }
                //4.调用阿里云AI  审核图片
                response = aliyunImageScanRequest.imageScanRequest(images);
                if ("review".equals(response)){//人工审核
                    updateWmNews(wmNews,(short)3, "图片需要人工审核");
                    return;
                } else if (!"pass".equals(response)){
                    //文章内容审核没有通过
                    updateWmNews(wmNews, (short) 2, "图片内容有违规行为");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            //审核通过以后查看发布的时间，如果发布时间大于当前时间则正常发布保存时间，否则修改状态为待发布
            if (wmNews.getPublishTime() != null) {
                if (wmNews.getPublishTime().getTime() > System.currentTimeMillis()) {
                    // 定时发布
                    updateWmNews(wmNews, (short) 8, "待发布");
                } else {
                    // 立即发布
                    reviewSuccessSaveAll(wmNews);
                }
            } else {
                // 立即发布
                reviewSuccessSaveAll(wmNews);
            }
        }
    }

    /**
     * 审核通过保存文章全部信息
     * 存入数据   ap_article_config   ap_article   ap_article_content  ap_author
     * @param wmNews
     */
    private void reviewSuccessSaveAll(WmNews wmNews) {
        ApAuthor apAuthor = null;
        // 保存ap_author表
        if(wmNews.getUserId() != null){
            WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
            if(wmUser != null && wmUser.getName() != null){
                apAuthor = apAuthorMapper.selectByAuthorName(wmUser.getName());
                if(apAuthor == null || apAuthor.getId() == null){
                    apAuthor = new ApAuthor();
                    apAuthor.setUserId(wmNews.getUserId());
                    apAuthor.setCreatedTime(new Date());
                    apAuthor.setType(2);
                    apAuthor.setName(wmUser.getName());
                    apAuthor.setWmUserId(wmUser.getId());
                    apAuthorMapper.insert(apAuthor);
                }
            }
        }

        // 保存 ap_article表
        ApArticle apArticle = new ApArticle();
        if(apAuthor != null){
            apArticle.setAuthorId(apAuthor.getId().longValue());
            apArticle.setAuthorName(apAuthor.getName());

        }
        apArticle.setPublishTime(new Date());
        apArticle.setCreatedTime(wmNews.getCreatedTime());
        Integer channelId = wmNews.getChannelId();
        if(channelId != null){
            AdChannel adChannel = adChannelMapper.selectByPrimaryKey(channelId);
            apArticle.setChannelId(channelId);
            apArticle.setChannelName(adChannel.getName());
        }
        apArticle.setLayout(wmNews.getType());
        apArticle.setTitle(wmNews.getTitle());
        String images = wmNews.getImages();
        if(images != null){
            String[] split = images.split(",");
            StringBuilder sb = new StringBuilder();
            for(int i = 0;i < split.length; i++){
                if(i > 0){
                    sb.append(",");
                }
                sb.append(FILE_SERVER_URL);
                sb.append(split[i]);
            }
            apArticle.setImages(sb.toString());
        }
        apArticleMapper.insert(apArticle);

        // ap_article_content
        ApArticleContent apArticleContent = new ApArticleContent();
        apArticleContent.setArticleId(apArticle.getId());
        apArticleContent.setContent(ZipUtils.gzip(wmNews.getContent()));
        apArticleContentMapper.insert(apArticleContent);

        // ap_article_config
        ApArticleConfig apArticleConfig = new ApArticleConfig();
        apArticleConfig.setArticleId(apArticle.getId());
        apArticleConfig.setIsComment(true);
        apArticleConfig.setIsDelete(false);
        apArticleConfig.setIsDown(false);
        apArticleConfig.setIsForward(true);
        apArticleConfigMapper.insert(apArticleConfig);

        // 创建es索引
        EsIndexEntity esIndexEntity = new EsIndexEntity();
        esIndexEntity.setId(apArticle.getId().longValue());
        // 防止出现空指针异常
        esIndexEntity.setChannelId(new Long(channelId));
        esIndexEntity.setContent(wmNews.getContent());
        esIndexEntity.setPublishTime(new Date());
        esIndexEntity.setStatus(new Long(1));
        esIndexEntity.setTitle(wmNews.getTitle());
        if(wmNews.getUserId() != null){
            esIndexEntity.setUserId(wmNews.getUserId());
        }
        esIndexEntity.setTag(ESIndexConstants.MEDIA_TAG);
        Index.Builder builder = new Index.Builder(esIndexEntity);
        builder.id(apArticle.getId().toString());
        builder.refresh(true);
        Index build = builder.index(ESIndexConstants.ARTICLE_INDEX)
                .type(ESIndexConstants.DEFAULT_DOC)
                .build();
        JestResult result = null;
        try {
            result = jestClient.execute(build);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("执行ES创建索引失败，message:{}",e.getMessage());
        }

        if(result == null || !result.isSucceeded()){
            log.error("插入更新索引失败：message:{}",result.getErrorMessage());
        }

        // 修改wmNews状态
        wmNews.setArticleId(apArticle.getId());
        updateWmNews(wmNews,(short)9, "审核成功");

        //文章审核成功
        ArticleAuditSuccess articleAuditSuccess = new ArticleAuditSuccess();
        articleAuditSuccess.setArticleId(apArticle.getId());
        articleAuditSuccess.setType(ArticleAuditSuccess.ArticleType.WEMEDIA);
        articleAuditSuccess.setChannelId(apArticle.getChannelId());
        kafkaSender.sendArticleAuditSuccessMessage(articleAuditSuccess);

        // 通知用户审核成功
        ApUserMessage apUserMessage = new ApUserMessage();
        apUserMessage.setUserId(wmNews.getUserId());
        apUserMessage.setCreatedTime(new Date());
        apUserMessage.setIsRead(false);
        apUserMessage.setContent("文章审核成功");
        apUserMessage.setType(108);// 文章审核通过
        apUserMessageMapper.insertSelective(apUserMessage);
    }

    /**
     * 审核通过保存全部信息
     */
    private void reviewSuccessSaveAll(WmNews wmNews, String content, String title) {
        //5.如果全部通过，插入数据，同时创建索引库
        //存入数据   ap_article_config   ap_article   ap_article_content  ap_author
        Integer channelId = wmNews.getChannelId();
        AdChannel apUserChannel = adChannelMapper.selectByPrimaryKey(channelId);
        String channelName = "";
        if (apUserChannel != null) {
            channelName = apUserChannel.getName();
        }

        Date createdTime = wmNews.getCreatedTime();
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        String authorName = "";
        if (wmUser != null) {
            authorName = wmUser.getName();
        }

        String reason = wmNews.getReason();
        Short type = wmNews.getType();

        //APP文章作者信息表
//            ApAuthor apAuthor = saveApAuthor(createdTime, wmUser, authorName);
        ApAuthor apAuthor = apAuthorMapper.selectByAuthorName(authorName);
        if (apAuthor == null || apAuthor.getId() == null) {
            apAuthor = new ApAuthor();
            apAuthor.setCreatedTime(createdTime);
            apAuthor.setName(authorName);
            apAuthor.setType(1);
            apAuthor.setUserId(wmUser.getApUserId());
            apAuthor.setWmUserId(Long.valueOf(wmUser.getId()));
            apAuthorMapper.insert(apAuthor);
        }

        //文章信息表，存储已发布的文章
        ApArticle apArticle = saveApArticle(title, wmNews.getImages(), channelId, channelName, createdTime, wmUser, authorName, apAuthor.getId(), type);

        //APP已发布文章配置表
        saveApArticleConfig(apArticle);

        //APP已发布文章内容表(内容加密)
        saveApArticleContent(ZipUtils.gzip(content), apArticle);

        //6.创建索引
        //channelId   content  id  pub_time  publishTime  status  title  userId  tag
        EsIndexEntity esIndexEntity = saveIndexEntity(wmNews, content, title, channelId, apArticle);

        Index.Builder builder = new Index.Builder(esIndexEntity);

        builder.id(apArticle.getId().toString());
        builder.refresh(true);
        Index index = builder.index(ESIndexConstants.ARTICLE_INDEX).type(ESIndexConstants.DEFAULT_DOC).build();
        JestResult result = null;
        try {
            result = jestClient.execute(index);
        } catch (IOException e) {
            log.error("执行ES创建索引失败：message:{}", e.getMessage());
        }
        if (result != null && !result.isSucceeded()) {
            //throw new RuntimeException(result.getErrorMessage() + "插入更新索引失败!");
            log.error("插入更新索引失败：message:{}", result.getErrorMessage());
        }

        //修改wmNews的状态为9
        wmNews.setArticleId(apArticle.getId());
        updateWmNews(wmNews, (short) 9, "审核成功");
        //通知用户 文章审核通过
        saveApUserMessage(wmNews, 108, "文章审核通过");
/*        try {
            // 发送热数据处理消息,此过程不影响审核结果
            ArticleAuditSuccess articleAuditSuccess = new ArticleAuditSuccess();
            articleAuditSuccess.setArticleId(apArticle.getId());
            articleAuditSuccess.setChannelId(apArticle.getChannelId());
            articleAuditSuccess.setType(ArticleAuditSuccess.ArticleType.WEMEDIA);
            kafkaSender.sendArticleAuditSuccessMessage(articleAuditSuccess);
        } catch (Exception e) {
            log.error("自动审核发送消息时错误：", e);
        }*/
    }

    /**
     * 创建索引
     *
     * @param wmNews
     * @param content
     * @param title
     * @param channelId
     * @param apArticle
     * @return
     */
    private EsIndexEntity saveIndexEntity(WmNews wmNews, String content, String title, Integer channelId, ApArticle apArticle) {
        EsIndexEntity esIndexEntity = new EsIndexEntity();
        esIndexEntity.setId(new Long(apArticle.getId()));
        esIndexEntity.setChannelId(new Long(channelId));
        esIndexEntity.setContent(content);
        esIndexEntity.setPublishTime(new Date());
        esIndexEntity.setStatus(new Long(1));
        esIndexEntity.setTitle(title);
        esIndexEntity.setUserId(wmNews.getUserId());
        esIndexEntity.setTag(ESIndexConstants.MEDIA_TAG);
        return esIndexEntity;
    }

    /**
     * 创建用户
     *
     * @param createdTime
     * @param wmUser
     * @param authorName
     */
    private ApAuthor saveApAuthor(Date createdTime, WmUser wmUser, String authorName) {
        ApAuthor apAuthor = new ApAuthor();
        apAuthor.setCreatedTime(createdTime);
        apAuthor.setUserId(wmUser.getId());
        apAuthor.setName(authorName);
        apAuthor.setType(2);
        apAuthorMapper.insert(apAuthor);
        return apAuthor;
    }

    /**
     * 保存文章内容
     *
     * @param content
     * @param apArticle
     */
    private void saveApArticleContent(String content, ApArticle apArticle) {
        ApArticleContent apArticleContent = new ApArticleContent();
        apArticleContent.setArticleId(apArticle.getId());
        apArticleContent.setContent(content);
        apArticleContentMapper.insert(apArticleContent);
    }

    /**
     * 保存文章配置信息
     *
     * @param apArticle
     */
    private void saveApArticleConfig(ApArticle apArticle) {
        ApArticleConfig apArticleConfig = new ApArticleConfig();
        apArticleConfig.setArticleId(apArticle.getId());
        apArticleConfig.setIsComment(true);
        apArticleConfig.setIsDelete(false);
        apArticleConfig.setIsDown(false);
        apArticleConfig.setIsForward(true);
        apArticleConfigMapper.insert(apArticleConfig);
    }

    /**
     * 保存文章信息
     *
     * @param title
     * @param images
     * @param channelId
     * @param channelName
     * @param createdTime
     * @param publishTime
     * @param wmUser
     * @param authorName
     * @return
     */
    private ApArticle saveApArticle(String title, String images, Integer channelId,
                                    String channelName, Date createdTime, WmUser wmUser,
                                    String authorName, Integer authorId, Short type) {
        ApArticle apArticle = new ApArticle();
        apArticle.setChannelId(channelId);
        apArticle.setChannelName(channelName);
        apArticle.setAuthorId(wmUser.getId());
        apArticle.setAuthorName(authorName);
        apArticle.setCreatedTime(createdTime);
        if (images != null) {
            String[] split = images.split(",");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append(FILE_SERVER_URL);
                sb.append(split[i]);
            }
            apArticle.setImages(sb.toString());
        }
        apArticle.setLayout(type);
        apArticle.setTitle(title);
        apArticle.setPublishTime(new Date());
        apArticle.setAuthorId(new Long(authorId));
        apArticle.setOrigin(true);
        apArticleMapper.insert(apArticle);
        return apArticle;
    }

    /**
     * 处理content  找出文本和图片列表
     *
     * @param images
     * @param sb
     * @param jsonArray
     */
    private void handlerTextAndImages(List<String> images, StringBuffer sb, JSONArray jsonArray) {
        for (Object obj : jsonArray) {
            JSONObject jsonObj = (JSONObject) obj;
            String type = (String) jsonObj.get("type");
            if ("image".equals(type)) {
                String value = (String) jsonObj.get("value");
                images.add(value);
            }
            if ("text".equals(type)) {
                sb.append(jsonObj.get("value"));
            }
        }
    }

    /**
     * 更改审核状态  更新状态及说明
     */
    private void updateWmNews(WmNews wmNews, short status, String message) {
        wmNews.setStatus(status);
        wmNews.setReason(message);
        wmNewsMapper.updateByPrimaryKeySelective(wmNews);
    }

    /**
     * 审核失败 在用户的通知消息表中存入消息，告知用户
     *
     * @param wmNews
     * @param i
     * @param s
     */
    private void saveApUserMessage(WmNews wmNews, int i, String s) {
        ApUserMessage apUserMessage = new ApUserMessage();
        apUserMessage.setUserId(wmNews.getUserId());
        apUserMessage.setCreatedTime(new Date());
        apUserMessage.setIsRead(false);
        apUserMessage.setType(i);
        apUserMessage.setContent(s);
        apUserMessage.setContent(s);
        apUserMessageMapper.insertSelective(apUserMessage);
    }
}