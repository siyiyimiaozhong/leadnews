package com.siyi.login.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.siyi.common.common.init","com.siyi.common.redis"})
public class InitConfig {
}