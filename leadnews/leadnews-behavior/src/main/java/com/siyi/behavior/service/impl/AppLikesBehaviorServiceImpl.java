package com.siyi.behavior.service.impl;

import com.siyi.behavior.kafka.BehaviorMessageSender;
import com.siyi.behavior.service.AppLikesBehaviorService;
import com.siyi.common.kafka.messages.behavior.UserLikesMessage;
import com.siyi.common.kafka.messages.behavior.UserReadMessage;
import com.siyi.common.zookeeper.sequence.Sequences;
import com.siyi.model.behavior.dtos.LikesBehaviorDto;
import com.siyi.model.behavior.pojos.ApBehaviorEntry;
import com.siyi.model.behavior.pojos.ApLikesBehavior;
import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.common.enums.AppHttpCodeEnum;
import com.siyi.model.mappers.app.ApBehaviorEntryMapper;
import com.siyi.model.mappers.app.ApLikesBehaviorMapper;
import com.siyi.model.user.pojos.ApUser;
import com.siyi.utils.common.BurstUtils;
import com.siyi.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class AppLikesBehaviorServiceImpl implements AppLikesBehaviorService {
 
    @Autowired
    private ApLikesBehaviorMapper apLikesBehaviorMapper;
    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;
    @Autowired
    private Sequences sequences;
    @Autowired
    private BehaviorMessageSender behaviorMessageSender;
 
    @Override
    public ResponseResult saveLikesBehavior(LikesBehaviorDto dto){
        ApUser user = AppThreadLocalUtils.getUser();
        // 用户和设备不能同时为空
        if(user==null&& dto.getEquipmentId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if(user!=null){
            userId = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, dto.getEquipmentId());
        // 行为实体找以及注册了，逻辑上这里是必定有值得，除非参数错误
        if(apBehaviorEntry==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApLikesBehavior alb = new ApLikesBehavior();
        alb.setId(sequences.sequenceApLikes());
        alb.setBehaviorEntryId(apBehaviorEntry.getId());
        alb.setCreatedTime(new Date());
        alb.setEntryId(dto.getEntryId());
        alb.setType(dto.getType());
        alb.setOperation(dto.getOperation());
        alb.setBurst(BurstUtils.encrypt(alb.getId(),alb.getBehaviorEntryId()));
        int insert = apLikesBehaviorMapper.insert(alb);
        if(insert==1){
            if(alb.getOperation()==ApLikesBehavior.Operation.LIKE.getCode()){
                behaviorMessageSender.sendMessagePlus(new UserLikesMessage(alb),userId,true);
            }else if(alb.getOperation()==ApLikesBehavior.Operation.CANCEL.getCode()){
                behaviorMessageSender.sendMessageReduce(new UserLikesMessage(alb),userId,true);
            }
        }
        return ResponseResult.okResult(insert);
    }
}