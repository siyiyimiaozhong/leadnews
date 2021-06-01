package com.siyi.user.apis;

import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.user.dtos.UserRelationDto;

/**
 * 关注或取消关注
 */
public interface UserRelationControllerApi {
    ResponseResult follow(UserRelationDto dto);
}