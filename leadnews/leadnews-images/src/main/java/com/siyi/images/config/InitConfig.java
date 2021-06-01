package com.siyi.images.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.siyi.common.common.init"})
public class InitConfig {
    @Value("${fast.dfs.image-path.prefix}")
    public static String PREFIX;
}