package com.siyi.migration.entity;

import com.siyi.model.article.pojos.ApArticle;

/**
 * Hbase相关回调操作的工具类
 */
public interface ArticleCallBack {
    public void callBack(ApArticle apArticle);
}