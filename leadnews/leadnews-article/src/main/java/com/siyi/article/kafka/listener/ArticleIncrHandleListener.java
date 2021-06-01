package com.siyi.article.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siyi.article.service.AppArticleService;
import com.siyi.common.kafka.KafkaListener;
import com.siyi.common.kafka.KafkaTopicConfig;
import com.siyi.common.kafka.messages.app.ArticleVisitStreamMessage;
import com.siyi.model.mess.app.ArticleVisitStreamDto;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 增量文章状态处理
 */
@Component
public class ArticleIncrHandleListener implements KafkaListener<String,String> {
    static Logger logger  = LoggerFactory.getLogger(ArticleIncrHandleListener.class);

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private AppArticleService appArticleService;

    @Override
    public String topic() {
        return kafkaTopicConfig.getArticleIncrHandle();
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> data, Consumer<?, ?> consumer) {
        logger.info("receive Article Incr Handle message:{}",data);
        String value = (String)data.value();
        try {
            ArticleVisitStreamMessage message = mapper.readValue(value, ArticleVisitStreamMessage.class);
            ArticleVisitStreamDto dto = message.getData();
            appArticleService.updateArticleView(dto);
        }catch (Exception e){
            logger.error("kafka send message[class:{}] to Article Incr Handle failed:{}","ArticleIncrHandle.class",e);
        }
    }
}