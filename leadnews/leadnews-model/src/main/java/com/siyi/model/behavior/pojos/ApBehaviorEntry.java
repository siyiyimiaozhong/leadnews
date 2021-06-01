package com.siyi.model.behavior.pojos;

import lombok.Data;
import lombok.Getter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * APP行为实体表,一个行为实体可能是用户或者设备，或者其它
 */
@Data
public class ApBehaviorEntry {
    private Integer id;
    private Short type;//实体类型  0:终端设备   1:用户
    private Integer entryId;//实体ID
    private Date createdTime;
    public String burst;//分片

    @Alias("ApBehaviorEntryEnumType")
    public enum Type{
        USER((short)1),EQUIPMENT((short)0);
        @Getter
        short code;
        Type(short code){
            this.code = code;
        }
    }

    public boolean isUser(){
        if(this.getType()!=null&&this.getType()== Type.USER.getCode()){
            return true;
        }
        return false;
    }
}