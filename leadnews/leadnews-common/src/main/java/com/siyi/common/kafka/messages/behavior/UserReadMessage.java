package com.siyi.common.kafka.messages.behavior;

import com.siyi.common.kafka.KafkaMessage;
import com.siyi.model.behavior.pojos.ApReadBehavior;

public class UserReadMessage extends KafkaMessage<ApReadBehavior> {

    public UserReadMessage(){}
    public UserReadMessage(ApReadBehavior data) {
        super(data);
    }

    @Override
    public String getType() {
        return "user-read";
    }
}