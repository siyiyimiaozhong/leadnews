package com.siyi.behavior.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.siyi.common.common.init","com.siyi.common.kafka"})
public class InitConfig {
}