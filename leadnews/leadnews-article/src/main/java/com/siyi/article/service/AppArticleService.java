package com.siyi.article.service;

import com.siyi.model.article.dtos.ArticleHomeDto;
import com.siyi.model.article.pojos.ApArticle;
import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.mess.app.ArticleVisitStreamDto;

import java.util.List;

public interface AppArticleService {
    /**
     * type   1 加载更多  2 加载更新
     * @param dto
     * @param type
     * @return
     */
    public List<ApArticle> load(ArticleHomeDto dto, Short type);

    /**
     * 更新阅读数
     * @param dto
     * @return
     */
    ResponseResult updateArticleView(ArticleVisitStreamDto dto);

    /**
     * 加载文章列表数据
     * @param type 1 加载更多  2 加载更新
     * @param dto 封装数据
     * @return 数据列表
     */
    ResponseResult loadV2(Short type, ArticleHomeDto dto, boolean firstPage);
}
