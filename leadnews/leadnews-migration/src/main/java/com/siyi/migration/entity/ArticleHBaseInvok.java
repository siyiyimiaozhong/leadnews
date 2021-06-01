package com.siyi.migration.entity;

import com.siyi.common.hbase.entity.HBaseInvok;
import com.siyi.model.article.pojos.ApArticle;
import lombok.Getter;
import lombok.Setter;

/**
 * 回调对象
 * Hbase 对回调对象的封装，以及对回调的invok执行对象
 */
@Setter
@Getter
public class ArticleHBaseInvok implements HBaseInvok {

    /**
     * 回调需要传输的对象
     */
    private ApArticle apArticle;
    /**
     * 回调需要对应的回调接口
     */
    private ArticleCallBack articleCallBack;

    public ArticleHBaseInvok(ApArticle apArticle, ArticleCallBack articleCallBack) {
        this.apArticle = apArticle;
        this.articleCallBack = articleCallBack;
    }

    /**
     * 执行回调方法
     */
    @Override
    public void invok() {
        if (null != apArticle && null != articleCallBack) {
            articleCallBack.callBack(apArticle);
        }
    }
}
