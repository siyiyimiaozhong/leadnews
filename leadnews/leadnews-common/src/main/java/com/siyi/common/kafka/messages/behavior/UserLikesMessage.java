package com.siyi.common.kafka.messages.behavior;

import com.siyi.common.kafka.KafkaMessage;
import com.siyi.model.behavior.pojos.ApLikesBehavior;

public class UserLikesMessage extends KafkaMessage<ApLikesBehavior> {

    public  UserLikesMessage(){}
    public UserLikesMessage(ApLikesBehavior data) {
        super(data);
    }

    @Override
    public String getType() {
        return "user-likes";
    }
}