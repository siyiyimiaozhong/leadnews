package com.siyi.behavior.service;

import com.siyi.model.behavior.dtos.UnLikesBehaviorDto;
import com.siyi.model.common.dtos.ResponseResult;

public interface AppUnLikesBehaviorService {
 
    /**
     * 存储不喜欢数据
     * @param dto
     * @return
     */
    public ResponseResult saveUnLikesBehavior(UnLikesBehaviorDto dto);
}