package com.siyi.user.service;

import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.user.dtos.UserRelationDto;

public interface AppUserRelationService {
    /**
     * 用户的关注或取消关注
     * @param dto
     * @return
     */
    public ResponseResult follow(UserRelationDto dto);
}