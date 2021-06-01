package com.siyi.model.mappers.app;

import com.siyi.model.behavior.pojos.ApReadBehavior;
import org.apache.ibatis.annotations.Param;

public interface ApReadBehaviorMapper {
    /**
     * 保存
     * @param record
     * @return
     */
    int insert(ApReadBehavior record);

    /**
     * 修改
     * @param record
     * @return
     */
    int update(ApReadBehavior record);

    /**
     * 根绝实体id查询阅读行为
     * @param burst
     * @param entryId
     * @param articleId
     * @return
     */
    ApReadBehavior selectByEntryId(@Param("burst") String burst,@Param("entryId") Integer entryId,@Param("articleId") Integer articleId);
}