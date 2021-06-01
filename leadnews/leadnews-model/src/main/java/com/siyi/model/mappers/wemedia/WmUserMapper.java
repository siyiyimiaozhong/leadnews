package com.siyi.model.mappers.wemedia;

import com.siyi.model.media.pojos.WmNews;
import com.siyi.model.media.pojos.WmUser;
import org.apache.ibatis.annotations.Param;

public interface WmUserMapper {
    WmUser selectByName(@Param("name") String name);

    WmUser selectById(@Param("id") Long id);
}