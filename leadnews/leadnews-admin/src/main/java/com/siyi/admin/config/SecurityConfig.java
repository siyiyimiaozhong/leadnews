package com.siyi.admin.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan("com.siyi.common.web.admin.security")
public class SecurityConfig {
}