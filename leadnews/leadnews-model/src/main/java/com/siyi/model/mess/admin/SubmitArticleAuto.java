package com.siyi.model.mess.admin;

import lombok.Data;

/**
 * 封装传递的消息，并且区分消息的类型,主要用于通知审核消息等
 */
@Data
public class SubmitArticleAuto {

    // 文章类型
    private ArticleType type;
    // 文章ID
    private Integer articleId;

    public enum ArticleType{
        WEMEDIA,CRAWLER;
    }
}
