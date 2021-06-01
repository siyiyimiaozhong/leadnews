package com.siyi.article.apis;

import com.siyi.model.article.dtos.ArticleHomeDto;
import com.siyi.model.common.dtos.ResponseResult;

public interface ArticleHomeControllerApi {
    /**
     * 加载首页文章
     * @param dto
     * @return
     */
    public ResponseResult load(ArticleHomeDto dto);

    /**
     * 加载更多
     * @param dto
     * @return
     */
    public ResponseResult loadMore(ArticleHomeDto dto);

    /**
     * 加载最新的文章信息
     * @param dto
     * @return
     */
    public ResponseResult loadNew(ArticleHomeDto dto);
}
