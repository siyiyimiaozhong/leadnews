package com.siyi.behavior.service;

import com.siyi.model.behavior.dtos.ShowBehaviorDto;
import com.siyi.model.common.dtos.ResponseResult;

public interface AppShowBehaviorService {
    ResponseResult saveShowBehavior(ShowBehaviorDto dto);
}
