package com.siyi.media.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan("com.siyi.common.web.wm.security")
public class SecurityConfig {
}