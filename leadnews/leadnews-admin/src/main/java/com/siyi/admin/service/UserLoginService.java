package com.siyi.admin.service;

import com.siyi.model.admin.pojos.AdUser;
import com.siyi.model.common.dtos.ResponseResult;

public interface UserLoginService {

    ResponseResult login(AdUser user);
}