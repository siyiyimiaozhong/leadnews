package com.siyi.common.kafka.messages;

import com.siyi.common.kafka.KafkaMessage;
import com.siyi.model.mess.admin.SubmitArticleAuto;

public class SubmitArticleAuthMessage extends KafkaMessage<SubmitArticleAuto> {

    public SubmitArticleAuthMessage(){}
    public SubmitArticleAuthMessage(SubmitArticleAuto data){
        super(data);
    }

    @Override
    public String getType() {
        return "submit-article-auth";
    }
}
