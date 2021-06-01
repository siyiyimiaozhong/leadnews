package com.siyi.images.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan("com.siyi.common.web.app.security")
public class SecurityConfig {

}