package com.siyi.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.siyi.common.common.init","com.siyi.common.mysql.core","com.siyi.common.quartz"})
@MapperScan("com.siyi.admin.dao")
public class InitConfig {
}