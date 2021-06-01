package com.siyi.user.service;

import com.siyi.model.behavior.dtos.FollowBehaviorDto;
import com.siyi.model.common.dtos.ResponseResult;

public interface AppFollowBehaviorService {
    /**
     * 存储关注数据
     * @param dto
     * @return
     */
    public ResponseResult saveFollowBehavior(FollowBehaviorDto dto);
}