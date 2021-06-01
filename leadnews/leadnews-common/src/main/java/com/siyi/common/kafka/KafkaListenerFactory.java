package com.siyi.common.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.kafka.config.AbstractKafkaListenerContainerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 实现在构造之后扫描实现了的KafkaListener接口的Bean，并自动注册成消费者监听器。
 */
@Component
public class KafkaListenerFactory implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(KafkaListenerFactory.class);

    @Autowired
    DefaultListableBeanFactory defaultListableBeanFactory;

    @Override
    public void afterPropertiesSet() {
        Map<String,KafkaListener> map = defaultListableBeanFactory.getBeansOfType(KafkaListener.class);
        for (String key : map.keySet()) {
            KafkaListener k = map.get(key);
            AbstractKafkaListenerContainerFactory factory = (AbstractKafkaListenerContainerFactory)defaultListableBeanFactory.getBean(k.factory());
            AbstractMessageListenerContainer container = factory.createContainer(k.topic());
            container.setupMessageListener(k);
            String beanName = k.getClass().getSimpleName()+"AutoListener" ;
            defaultListableBeanFactory.registerSingleton(beanName,container);
            logger.info("add auto listener [{}]",beanName);
        }
    }
}