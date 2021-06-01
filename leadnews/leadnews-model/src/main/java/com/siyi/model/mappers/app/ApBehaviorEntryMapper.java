package com.siyi.model.mappers.app;

import com.siyi.model.behavior.pojos.ApBehaviorEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApBehaviorEntryMapper {
    public ApBehaviorEntry selectByUserIdOrEquipmentId(@Param("userId") Long userId, @Param("equipmentId") Integer equipmentId);

    List<ApBehaviorEntry> selectAllEntry();
}
