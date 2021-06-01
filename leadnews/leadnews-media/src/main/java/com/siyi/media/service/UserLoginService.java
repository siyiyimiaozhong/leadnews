package com.siyi.media.service;

import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.media.pojos.WmUser;

public interface UserLoginService {
    ResponseResult login(WmUser user);
}