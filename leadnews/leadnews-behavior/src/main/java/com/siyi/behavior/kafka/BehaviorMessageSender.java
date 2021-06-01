package com.siyi.behavior.kafka;

import com.siyi.common.kafka.KafkaMessage;
import com.siyi.common.kafka.KafkaSender;
import com.siyi.common.kafka.messages.UpdateArticleMessage;
import com.siyi.common.kafka.messages.behavior.UserLikesMessage;
import com.siyi.common.kafka.messages.behavior.UserReadMessage;
import com.siyi.model.article.pojos.ApCollection;
import com.siyi.model.behavior.pojos.ApLikesBehavior;
import com.siyi.model.mess.app.UpdateArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BehaviorMessageSender {

    @Autowired
    private KafkaSender kafkaSender;

    /**
     * 发送+1的消息
     * @param message
     * @param isSendToArticle
     */
    @Async
    public void sendMessagePlus(KafkaMessage message,Long apUserId,boolean isSendToArticle){
        if(isSendToArticle){
            UpdateArticleMessage temp = parseMessage(message,apUserId,1);
            if(temp!=null)
                kafkaSender.sendArticleUpdateBus(temp);
        }
    }

    /**
     * 发送-1的消息
     * @param message
     * @param isSendToArticle
     */
    @Async
    public void sendMessageReduce(KafkaMessage message,Long apUserId,boolean isSendToArticle){
        if(isSendToArticle){
            UpdateArticleMessage temp = parseMessage(message,apUserId,-1);
            if(temp!=null)
                kafkaSender.sendArticleUpdateBus(temp);
        }
    }

    /**
     * 转换行为消息为修改位置的消息
     * @param message
     * @param step
     * @return
     */
    private UpdateArticleMessage parseMessage(KafkaMessage message, Long apUserId, int step){
        UpdateArticle ua = new UpdateArticle();
        if(apUserId != null) ua.setApUserId(apUserId.intValue());
        if(message instanceof UserLikesMessage){
            // 转换为收藏的消息
            UserLikesMessage m = (UserLikesMessage)message;
            // 只处理文章数据的点赞
            if(m.getData().getType()== ApLikesBehavior.Type.ARTICLE.getCode()){
                ua.setType(UpdateArticle.UpdateArticleType.LIKES);
                ua.setAdd(step);
                ua.setArticleId(m.getData().getEntryId());
                ua.setBehaviorId(m.getData().getBehaviorEntryId());
            }
        }else if(message instanceof UserReadMessage){
            UserReadMessage m = (UserReadMessage) message;
            ua.setType(UpdateArticle.UpdateArticleType.VIEWS);
            ua.setAdd(step);
            ua.setArticleId(m.getData().getArticleId());
            ua.setBehaviorId(m.getData().getEntryId());
        }
        if(ua.getArticleId() != null){
            return new UpdateArticleMessage(ua);
        }
        return null;
    }
}