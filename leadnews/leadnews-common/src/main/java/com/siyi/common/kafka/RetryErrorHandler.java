package com.siyi.common.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.stereotype.Component;

/**
 * 用于在消费者解析消息出现错误时，重新放回消息到队列中，
 * 并设置超过一个小时或者超过10次处理错误的消息丢弃，
 * 避免消息无限滚动；然后这类消息可以通过日志搜索查找出数据补偿重试。
 */
@Component
public class RetryErrorHandler extends LoggingErrorHandler {
    private static Logger logger = LoggerFactory.getLogger(RetryErrorHandler.class);
    private static  final  int RETRY_COUNT = 10;
    private static  final  int TIME_OUT = 3_600_000;//1个小时超时

    @Autowired
    KafkaSender sender;
    @Autowired
    ObjectMapper mapper;

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> record) {
        super.handle(thrownException, record);
        if (record != null) {
            try{
                KafkaMessage<?> message = mapper.readValue((String)record.value(),KafkaMessage.class);
                message.addRetry();
                long time = System.currentTimeMillis()-message.getTime();
                if(message.getRetry()>RETRY_COUNT || time>TIME_OUT){
                    logger.info("超时或者尝试{}次后，抛弃消息[topic:{}][{}]",RETRY_COUNT,record.topic(),record.value());
                }else{
                    this.sender.sendMessage(record.topic(),(String)record.key(),message);
                    logger.info("处理失败重新回滚到队列[retry:{}][topic:{}][key:{}]",message.getRetry(),record.topic(),record.key());
                }
            }catch (Exception e){
                sender.sendMessageNoWrap(record.topic(),(String) record.key(),(String) record.value());
            }

        }
    }

}