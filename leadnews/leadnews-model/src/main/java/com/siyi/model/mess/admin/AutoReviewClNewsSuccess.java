package com.siyi.model.mess.admin;


import com.siyi.model.article.pojos.ApArticleConfig;
import com.siyi.model.article.pojos.ApArticleContent;
import com.siyi.model.article.pojos.ApAuthor;
import lombok.Data;

@Data
public class AutoReviewClNewsSuccess {
    private ApArticleConfig apArticleConfig;
    private ApArticleContent apArticleContent;
    private ApAuthor apAuthor;

}
