package com.siyi.behavior.service.impl;

import com.siyi.behavior.service.AppUnLikesBehaviorService;
import com.siyi.model.behavior.dtos.UnLikesBehaviorDto;
import com.siyi.model.behavior.pojos.ApBehaviorEntry;
import com.siyi.model.behavior.pojos.ApUnlikesBehavior;
import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.common.enums.AppHttpCodeEnum;
import com.siyi.model.mappers.app.ApBehaviorEntryMapper;
import com.siyi.model.mappers.app.ApUnlikesBehaviorMapper;
import com.siyi.model.user.pojos.ApUser;
import com.siyi.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class AppUnLikesBehaviorServiceImpl implements AppUnLikesBehaviorService {
 
    @Autowired
    private ApUnlikesBehaviorMapper apUnLikesBehaviorMapper;
    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;
 
    @Override
    public ResponseResult saveUnLikesBehavior(UnLikesBehaviorDto dto){
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
        ApUnlikesBehavior alb = new ApUnlikesBehavior();
        alb.setEntryId(apBehaviorEntry.getId());
        alb.setCreatedTime(new Date());
        alb.setArticleId(dto.getArticleId());
        alb.setType(dto.getType());
        return ResponseResult.okResult(apUnLikesBehaviorMapper.insert(alb));
    }
}