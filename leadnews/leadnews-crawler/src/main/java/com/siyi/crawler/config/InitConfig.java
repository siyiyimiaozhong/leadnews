package com.siyi.crawler.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan({"com.siyi.common.common.init", "com.siyi.common.mysql.core",
        "com.siyi.common.kafka","com.siyi.common.quartz"})
@EnableScheduling
public class InitConfig {
}
