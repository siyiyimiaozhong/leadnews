package com.siyi.admin.apis;

import com.siyi.model.admin.pojos.AdUser;
import com.siyi.model.common.dtos.ResponseResult;

public interface LoginControllerApi{
    public ResponseResult login(AdUser user);
}