package com.siyi.login.apis;

import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.user.pojos.ApUser;
import org.springframework.web.bind.annotation.RequestBody;

public interface LoginControllerApi {

    public ResponseResult login(ApUser user);
}