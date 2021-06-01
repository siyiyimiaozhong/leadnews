package com.siyi.migration.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siyi.common.kafka.KafkaListener;
import com.siyi.common.kafka.KafkaTopicConfig;
import com.siyi.common.kafka.messages.app.ApHotArticleMessage;
import com.siyi.migration.service.ApHotArticleService;
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
public class MigrationHotArticleListener implements KafkaListener<String, String> {
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
    /**
     * 热点文章service注入
     */
    @Autowired
    private ApHotArticleService apHotArticleService;


    @Override
    public String topic() {
        return kafkaTopicConfig.getHotArticle();
    }

    /**
     * 监听消息
     *
     * @param data
     * @param consumer
     */
    @Override
    public void onMessage(ConsumerRecord<String, String> data, Consumer<?, ?> consumer) {
        log.info("kafka接收到热数据同步消息:{}", data);
        String value = (String) data.value();
        if (null != value) {
            ApHotArticleMessage message = null;
            try {
                message = mapper.readValue(value, ApHotArticleMessage.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Integer articleId = message.getData().getArticleId();
            if (null != articleId) {
                //调用方法 将HBAESE中的热数据进行同步
                apHotArticleService.hotApArticleSync(articleId);
            }
        }
    }
}