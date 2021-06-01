package com.siyi.common.kafka.messages;

import com.siyi.common.kafka.KafkaMessage;
import com.siyi.model.mess.app.UpdateArticle;

public class UpdateArticleMessage extends KafkaMessage<UpdateArticle> {

    public UpdateArticleMessage(){}

    public UpdateArticleMessage(UpdateArticle data){
        super(data);
    }

    @Override
    public String getType() {
        return "update-article";
    }
}
