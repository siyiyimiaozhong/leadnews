package com.siyi.model.behavior.pojos;

import com.siyi.model.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;

@Data
public class ApShareBehavior {
    private Long id;
    @IdEncrypt
    private Integer entryId;
    @IdEncrypt
    private Integer articleId;
    private Short type;
    private Date createdTime;
}