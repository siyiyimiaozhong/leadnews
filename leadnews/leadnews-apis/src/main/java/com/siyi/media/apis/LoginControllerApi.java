package com.siyi.media.apis;

import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.media.pojos.WmUser;

public interface LoginControllerApi {
    public ResponseResult login(WmUser user);
}