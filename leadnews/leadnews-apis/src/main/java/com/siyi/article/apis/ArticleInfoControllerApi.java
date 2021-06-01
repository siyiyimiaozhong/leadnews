package com.siyi.article.apis;

import com.siyi.model.article.dtos.ArticleInfoDto;
import com.siyi.model.common.dtos.ResponseResult;

/**
 * 首頁文章
 */
public interface ArticleInfoControllerApi {
    /**
     * 加載首頁详情
     * @param dto 封装参数对象
     * @return 文章详情
     */
    ResponseResult loadArticleInfo(ArticleInfoDto dto);

    /**
     * 加载文章详情的行为内容
     * @param dto
     * @return
     */
    ResponseResult loadArticleBehavior( ArticleInfoDto dto);
}