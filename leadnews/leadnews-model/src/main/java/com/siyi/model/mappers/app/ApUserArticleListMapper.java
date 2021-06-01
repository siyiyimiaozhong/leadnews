package com.siyi.model.mappers.app;

import com.siyi.model.article.dtos.ArticleHomeDto;
import com.siyi.model.user.pojos.ApUser;
import com.siyi.model.user.pojos.ApUserArticleList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApUserArticleListMapper {
    public List<ApUserArticleList> loadArticleIdListByUser(@Param("user") ApUser user,@Param("dto") ArticleHomeDto dto,@Param("type") Short type);
}
