package com.siyi.model.mappers.app;

import com.siyi.model.user.pojos.ApUser;
import org.apache.ibatis.annotations.Param;

public interface ApUserMapper {
    ApUser selectById(@Param("id") Integer id);

    ApUser selectByApPhone(@Param("phone") String phone);
}