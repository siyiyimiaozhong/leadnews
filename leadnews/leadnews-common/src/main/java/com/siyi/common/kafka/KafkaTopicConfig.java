package com.siyi.common.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix="kafka.topic")
@PropertySource("classpath:kafka.properties")
public class KafkaTopicConfig {
//    private String adminTest;
    private String submitArticleAuth;
    private String articleAuditSuccess;
    // 更新文章数据的消息topic
    private String articleUpdateBus;
    //文章增量流处理完毕 处理结果监听主题
    private String articleIncrHandle;
    //热文章消息主题
    private String hotArticle;
}