package com.siyi.model.user.pojos;

import lombok.Data;
import lombok.Setter;

import java.util.Date;

/**
 * 用户文章列表
 */
@Data
public class ApUserArticleList {
    private Integer id;
    private Integer userId;//用户id
    private Integer channelId;//频道id
    private Integer articleId;//动态id
    private Boolean isShow;//是否展示
    private Date recommendTime;//推荐时间
    private Boolean isRead;//是否阅读
    private Integer strategyId;//推荐算法

}