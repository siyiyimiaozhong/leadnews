package com.siyi.login.service;

import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.user.pojos.ApUser;

public interface ApUserLoginService {
    /**
     * 用户登录验证
     * @param user
     * @return
     */
    ResponseResult loginAuth(ApUser user);

    /**
     * 用户登录验证V2
     * @param user
     * @return
     */
    ResponseResult loginAuthV2(ApUser user);
}