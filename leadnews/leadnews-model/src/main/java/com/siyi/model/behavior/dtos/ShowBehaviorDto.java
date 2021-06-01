package com.siyi.model.behavior.dtos;


import com.siyi.model.annotation.IdEncrypt;
import com.siyi.model.article.pojos.ApArticle;
import lombok.Data;

import java.util.List;

@Data
public class ShowBehaviorDto {
    // 设备ID
    @IdEncrypt
    Integer equipmentId;
    //文章列表信息
    List<ApArticle> articleIds;
}
