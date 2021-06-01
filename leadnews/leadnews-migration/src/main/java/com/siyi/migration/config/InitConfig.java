package com.siyi.migration.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan({"com.siyi.common.common.init","com.siyi.common.mongo","com.siyi.common.mysql.core",
        "com.siyi.common.quartz","com.siyi.common.hbase","com.siyi.common.kafka"})
@EnableScheduling
public class InitConfig {
}