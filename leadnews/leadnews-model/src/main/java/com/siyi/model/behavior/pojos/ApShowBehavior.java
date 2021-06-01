package com.siyi.model.behavior.pojos;

import lombok.Data;
import lombok.Setter;

import java.util.Date;

/**
 * 文章展现行为
 */
@Data
public class ApShowBehavior {
    private Integer id;
    private Integer entryId;//实体id
    private Integer articleId;//文章id
    private Boolean isClick;//是否点击
    private Date showTime;//文章加载时间
    private Date createdTime;//登录时间

}