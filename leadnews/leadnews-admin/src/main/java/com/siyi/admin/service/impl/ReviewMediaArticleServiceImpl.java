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
     * ????????????
     */
    private static final Integer MAIN_PICTURE_SIZE = 5;

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    /**
     * ???????????????????????????
     *
     * @param newsId ??????id
     */
    @Override
    public void autoReviewArticleByMedia(Integer newsId) {
        //1.????????????id????????????????????????????????????????????????
//        WmNews wmNews = wmNewsMapper.findbyNewsIdAndStatus(newsId, 1);
        WmNews wmNews = wmNewsMapper.selectNewsDetailByPrimaryKey(newsId);

        //????????????   ?????????????????????????????????
        if (wmNews != null && wmNews.getStatus() == 4) {
            reviewSuccessSaveAll(wmNews);
            return;
        }
        //??????????????????????????????????????????????????????
        if (wmNews != null && wmNews.getStatus() == 8 && wmNews.getPublishTime()!=null && wmNews.getPublishTime().getTime() > new Date().getTime()) {
            reviewSuccessSaveAll(wmNews);
            return;
        }

        if (wmNews != null && wmNews.getStatus() == 1) {
            //2.???????????????????????????????????? ?????????
            String content = wmNews.getContent();//????????????
            String title = wmNews.getTitle();//????????????
            double degree = Compute.SimilarDegree(content, title);
            if (degree <= 0) {
                //??????????????????????????????
                updateWmNews(wmNews,(short)2,"??????????????????????????????");
                return;
            }
            //TODO ?????????????????????????????????????????????????????????????????????
            /*double semblance = SimHashUtils.getSemblance(title, content, 64);
            if(semblance<review_article_pass && semblance>review_article_review){
                //???????????????????????????
                updateWmNews(wmNews, (short) 3, "????????????");
                return;
            }
            if(semblance<review_article_review){
                //?????????????????????????????????
                updateWmNews(wmNews, (short) 2, "????????????????????????");
                //?????????????????????????????????
                saveApUserMessage(wmNews, 109, "????????????????????????????????????????????????");
                return;
            }*/

            //3.?????????????????????????????????
            List<String> images = new ArrayList<>();
            StringBuffer sb = new StringBuffer();
            JSONArray jsonArray = JSON.parseArray(content);
            handlerTextAndImages(images, sb, jsonArray);
            //????????????????????????
            if (images.size() > MAIN_PICTURE_SIZE) {
                images = images.subList(0, MAIN_PICTURE_SIZE);
            }
            //TODO ???????????? ?????????????????????????????????????????????
            /*try {
                String response = aliyunTextScanRequest.textScanRequest(sb.toString());
                if ("review".equals(response)){//????????????
                    updateWmNews(wmNews,(short)3, "??????????????????");
                    return;
                } else if (!"pass".equals(response)){
                    //??????????????????????????????
                    updateWmNews(wmNews, (short) 2, "???????????????????????????");
                    return;
                }
                //4.???????????????AI  ????????????
                response = aliyunImageScanRequest.imageScanRequest(images);
                if ("review".equals(response)){//????????????
                    updateWmNews(wmNews,(short)3, "????????????????????????");
                    return;
                } else if (!"pass".equals(response)){
                    //??????????????????????????????
                    updateWmNews(wmNews, (short) 2, "???????????????????????????");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (wmNews.getPublishTime() != null) {
                if (wmNews.getPublishTime().getTime() > System.currentTimeMillis()) {
                    // ????????????
                    updateWmNews(wmNews, (short) 8, "?????????");
                } else {
                    // ????????????
                    reviewSuccessSaveAll(wmNews);
                }
            } else {
                // ????????????
                reviewSuccessSaveAll(wmNews);
            }
        }
    }

    /**
     * ????????????????????????????????????
     * ????????????   ap_article_config   ap_article   ap_article_content  ap_author
     * @param wmNews
     */
    private void reviewSuccessSaveAll(WmNews wmNews) {
        ApAuthor apAuthor = null;
        // ??????ap_author???
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

        // ?????? ap_article???
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

        // ??????es??????
        EsIndexEntity esIndexEntity = new EsIndexEntity();
        esIndexEntity.setId(apArticle.getId().longValue());
        // ???????????????????????????
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
            log.error("??????ES?????????????????????message:{}",e.getMessage());
        }

        if(result == null || !result.isSucceeded()){
            log.error("???????????????????????????message:{}",result.getErrorMessage());
        }

        // ??????wmNews??????
        wmNews.setArticleId(apArticle.getId());
        updateWmNews(wmNews,(short)9, "????????????");

        //??????????????????
        ArticleAuditSuccess articleAuditSuccess = new ArticleAuditSuccess();
        articleAuditSuccess.setArticleId(apArticle.getId());
        articleAuditSuccess.setType(ArticleAuditSuccess.ArticleType.WEMEDIA);
        articleAuditSuccess.setChannelId(apArticle.getChannelId());
        kafkaSender.sendArticleAuditSuccessMessage(articleAuditSuccess);

        // ????????????????????????
        ApUserMessage apUserMessage = new ApUserMessage();
        apUserMessage.setUserId(wmNews.getUserId());
        apUserMessage.setCreatedTime(new Date());
        apUserMessage.setIsRead(false);
        apUserMessage.setContent("??????????????????");
        apUserMessage.setType(108);// ??????????????????
        apUserMessageMapper.insertSelective(apUserMessage);
    }

    /**
     * ??????????????????????????????
     */
    private void reviewSuccessSaveAll(WmNews wmNews, String content, String title) {
        //5.?????????????????????????????????????????????????????????
        //????????????   ap_article_config   ap_article   ap_article_content  ap_author
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

        //APP?????????????????????
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

        //??????????????????????????????????????????
        ApArticle apArticle = saveApArticle(title, wmNews.getImages(), channelId, channelName, createdTime, wmUser, authorName, apAuthor.getId(), type);

        //APP????????????????????????
        saveApArticleConfig(apArticle);

        //APP????????????????????????(????????????)
        saveApArticleContent(ZipUtils.gzip(content), apArticle);

        //6.????????????
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
            log.error("??????ES?????????????????????message:{}", e.getMessage());
        }
        if (result != null && !result.isSucceeded()) {
            //throw new RuntimeException(result.getErrorMessage() + "????????????????????????!");
            log.error("???????????????????????????message:{}", result.getErrorMessage());
        }

        //??????wmNews????????????9
        wmNews.setArticleId(apArticle.getId());
        updateWmNews(wmNews, (short) 9, "????????????");
        //???????????? ??????????????????
        saveApUserMessage(wmNews, 108, "??????????????????");
/*        try {
            // ???????????????????????????,??????????????????????????????
            ArticleAuditSuccess articleAuditSuccess = new ArticleAuditSuccess();
            articleAuditSuccess.setArticleId(apArticle.getId());
            articleAuditSuccess.setChannelId(apArticle.getChannelId());
            articleAuditSuccess.setType(ArticleAuditSuccess.ArticleType.WEMEDIA);
            kafkaSender.sendArticleAuditSuccessMessage(articleAuditSuccess);
        } catch (Exception e) {
            log.error("????????????????????????????????????", e);
        }*/
    }

    /**
     * ????????????
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
     * ????????????
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
     * ??????????????????
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
     * ????????????????????????
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
     * ??????????????????
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
     * ??????content  ???????????????????????????
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
     * ??????????????????  ?????????????????????
     */
    private void updateWmNews(WmNews wmNews, short status, String message) {
        wmNews.setStatus(status);
        wmNews.setReason(message);
        wmNewsMapper.updateByPrimaryKeySelective(wmNews);
    }

    /**
     * ???????????? ?????????????????????????????????????????????????????????
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