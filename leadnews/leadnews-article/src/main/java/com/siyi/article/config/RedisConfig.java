package com.siyi.article.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.siyi.common.redis")
public class RedisConfig {
}