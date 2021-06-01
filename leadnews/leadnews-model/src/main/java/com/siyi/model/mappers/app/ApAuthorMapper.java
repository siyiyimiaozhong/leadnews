package com.siyi.model.mappers.app;

import com.siyi.model.article.pojos.ApAuthor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApAuthorMapper {
    ApAuthor selectById(@Param("id") Integer id);

    ApAuthor selectByAuthorName(String authorName);

    void insert(ApAuthor apAuthor);

    List<ApAuthor> selectByIds(List<Integer> ids);
}