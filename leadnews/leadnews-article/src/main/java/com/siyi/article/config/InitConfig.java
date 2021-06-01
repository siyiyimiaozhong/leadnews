package com.siyi.article.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan({"com.siyi.common.mysql.core","com.siyi.common.common.init",
        "com.siyi.common.quartz","com.siyi.common.kafka","com.siyi.common.kafkastream"})
@EnableScheduling
public class InitConfig {
}