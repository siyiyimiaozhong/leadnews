package com.siyi.article.service;

import com.siyi.model.article.dtos.ArticleInfoDto;
import com.siyi.model.common.dtos.ResponseResult;

public interface AppArticleInfoService {

    /**
     * 加载文章详情内容
     * @param articleId
     * @return
     */
    public ResponseResult getArticleInfo(Integer articleId);

    /**
     * 加载文章详情的初始化配置信息，比如关注、喜欢、不喜欢、阅读位置等
     * @param dto
     * @return
     */
    ResponseResult loadArticleBehavior(ArticleInfoDto dto);
}