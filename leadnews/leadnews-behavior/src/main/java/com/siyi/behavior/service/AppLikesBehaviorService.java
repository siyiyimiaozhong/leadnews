package com.siyi.behavior.service;

import com.siyi.model.behavior.dtos.LikesBehaviorDto;
import com.siyi.model.common.dtos.ResponseResult;

public interface AppLikesBehaviorService {
    /**
     * 存储喜欢数据
     * @param dto
     * @return
     */
    public ResponseResult saveLikesBehavior(LikesBehaviorDto dto);
}