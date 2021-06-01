package com.siyi.common.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siyi.common.kafka.messages.ArticleAuditSuccessMessage;
import com.siyi.common.kafka.messages.SubmitArticleAuthMessage;
import com.siyi.common.kafka.messages.app.ApHotArticleMessage;
import com.siyi.model.article.pojos.ApHotArticles;
import com.siyi.model.mess.admin.ArticleAuditSuccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 所有发送消息的方法统一管理器，通过kafkaTemplate发送。
 */
@Component
public class KafkaSender {

    Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    /**
     * 发送一个消息
     * @param topic
     * @param key
     * @param message
     */
    public void sendMessage(String topic, String key, KafkaMessage<?> message){
        try {
            this.kafkaTemplate.send(topic, key, mapper.writeValueAsString(message));
        }catch (Exception e){
            logger.error("send message to [{}] error:",topic,e);
        }
    }

    /**
     * 发送一个不包装的消息
     * 只能是内部使用，拒绝业务上使用
     * @param topic
     * @param key
     * @param message
     */
    public void sendMessageNoWrap(String topic, String key, String message){
        try {
            this.kafkaTemplate.send(topic, key, message);
        }catch (Exception e){
            logger.error("send message to [{}] error:",topic,e);
        }
    }

    /**
     * 发送审核文章的消息
     * @param message
     */
    public void sendSubmitArticleAuthMessage(SubmitArticleAuthMessage message){
        this.sendMessage(kafkaTopicConfig.getSubmitArticleAuth(), UUID.randomUUID().toString(),message);
    }

    /**
     * 发送修改文章请求消息
     * @param message
     */
    public void sendArticleUpdateBus(KafkaMessage message) {
        this.sendMessage(kafkaTopicConfig.getArticleUpdateBus(), UUID.randomUUID().toString(), message);
    }

    /**
     * 发送处理热文章信息
     *
     * @param message
     */
    public void sendHotArticleMessage(ApHotArticles message) {
        ApHotArticleMessage temp = new ApHotArticleMessage();
        temp.setData(message);
        this.sendMessage(kafkaTopicConfig.getHotArticle(), UUID.randomUUID().toString(), temp);
    }

    /**
     * 发送审核成功消息
     */
    public void sendArticleAuditSuccessMessage(ArticleAuditSuccess message) {
        ArticleAuditSuccessMessage temp = new ArticleAuditSuccessMessage();
        temp.setData(message);
        this.sendMessage(kafkaTopicConfig.getArticleAuditSuccess(), UUID.randomUUID().toString(), temp);
    }
}