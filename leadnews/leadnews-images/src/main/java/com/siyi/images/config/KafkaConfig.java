package com.siyi.images.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.siyi.common.kafka","com.siyi.common.kafkastream"})
public class KafkaConfig {
}
