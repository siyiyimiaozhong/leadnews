package com.siyi.behavior.service;

import com.siyi.model.behavior.dtos.ReadBehaviorDto;
import com.siyi.model.common.dtos.ResponseResult;

public interface AppReadBehaviorService {
    /**
     * 存储阅读数据
     * @param dto
     * @return
     */
    public ResponseResult saveReadBehavior(ReadBehaviorDto dto);
}