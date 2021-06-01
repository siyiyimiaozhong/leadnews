package com.siyi.common.kafka.messages.app;

import com.siyi.common.kafka.KafkaMessage;
import com.siyi.model.mess.app.ArticleVisitStreamDto;

public class ArticleVisitStreamMessage extends KafkaMessage<ArticleVisitStreamDto> {

    @Override
    public String getType() {
        return "article-visit-stream";
    }
}