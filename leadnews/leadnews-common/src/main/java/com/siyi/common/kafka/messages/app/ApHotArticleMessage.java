package com.siyi.common.kafka.messages.app;

import com.siyi.common.kafka.KafkaMessage;
import com.siyi.model.article.pojos.ApHotArticles;

public class ApHotArticleMessage extends KafkaMessage<ApHotArticles> {

    public ApHotArticleMessage(){}

    public ApHotArticleMessage(ApHotArticles data){
        super(data);
    }

    @Override
    public String getType() {
        return "hot-article";
    }
}