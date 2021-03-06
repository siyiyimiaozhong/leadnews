package com.siyi.migration.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siyi.common.kafka.KafkaListener;
import com.siyi.common.kafka.KafkaTopicConfig;
import com.siyi.common.kafka.messages.ArticleAuditSuccessMessage;
import com.siyi.migration.service.ArticleQuantityService;
import com.siyi.model.mess.admin.ArticleAuditSuccess;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 热点文章监听类
 */
@Component
@Log4j2
public class MigrationAuditSucessArticleListener implements KafkaListener<String, String> {
    /**
     * 通用转换mapper
     */
    @Autowired
    private ObjectMapper mapper;
    /**
     * kafka 主题 配置
     */
    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    @Autowired
    private ArticleQuantityService articleQuantityService;


    @Override
    public String topic() {
        return kafkaTopicConfig.getArticleAuditSuccess();
    }

    /**
     * 监听消息
     *
     * @param data
     * @param consumer
     */
    @Override
    public void onMessage(ConsumerRecord<String, String> data, Consumer<?, ?> consumer) {
        log.info("kafka接收到审核通过消息:{}", data);
        String value = (String) data.value();
        if (null != value) {
            ArticleAuditSuccessMessage message = null;
            try {
                message = mapper.readValue(value, ArticleAuditSuccessMessage.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArticleAuditSuccess auto = message.getData();
            if (null != auto) {
                //调用方法 将HBAESE中的热数据进行同步
                Integer articleId = auto.getArticleId();
                if (null != articleId) {
                    articleQuantityService.dbToHbase(articleId);
                }
            }
        }

    }
}