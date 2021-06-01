package com.siyi.model.mappers.app;

import com.siyi.model.behavior.pojos.ApShowBehavior;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApShowBehaviorMapper {
    public List<ApShowBehavior> selectListByEntryIdAndArticleIds(@Param("entryId") Integer id,@Param("articleIds") Integer[] articleIds);

    public void saveShowBehavior(@Param("articleIds") Integer[] articleIds,@Param("entryId") Integer id);
}
