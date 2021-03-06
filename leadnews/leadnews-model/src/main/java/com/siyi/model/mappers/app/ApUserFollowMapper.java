package com.siyi.model.mappers.app;

import com.siyi.model.user.pojos.ApUserFollow;
import org.apache.ibatis.annotations.Param;

public interface ApUserFollowMapper {
    ApUserFollow selectByFollowId(@Param("burst") String burst,@Param("userId") Long userId,@Param("followId") Integer followId);

    int insert(ApUserFollow record);

    int deleteByFollowId(@Param("burst") String burst,@Param("userId") Long userId,@Param("followId") Integer followId);
}