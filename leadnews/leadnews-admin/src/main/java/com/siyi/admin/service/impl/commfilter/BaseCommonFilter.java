package com.siyi.admin.service.impl.commfilter;

import com.siyi.model.admin.dtos.CommonDto;
import com.siyi.model.admin.dtos.CommonWhereDto;
import com.siyi.model.admin.pojos.AdUser;

/**
 * 通用过滤器类 后置增强类
 */
public interface BaseCommonFilter {

    void doListAfter(AdUser user, CommonDto dto);
    void doUpdateAfter(AdUser user,CommonDto dto);
    void doInsertAfter(AdUser user, CommonDto dto);
    void doDeleteAfter(AdUser user, CommonDto dto);

    /**
     * 获取更新字段里面的值
     * @param field
     * @param dto
     * @return
     */
    default CommonWhereDto findUpdateValue(String field,CommonDto dto){
        if(dto!=null){
            for (CommonWhereDto cw : dto.getSets()){
                if(field.equals(cw.getFiled())){
                    return cw;
                }
            }
        }
        return null;
    }

    /**
     * 获取查询字段里面的值
     * @param field
     * @param dto
     * @return
     */
    default CommonWhereDto findWhereValue(String field, CommonDto dto){
        if(dto!=null){
            for (CommonWhereDto cw : dto.getWhere()){
                if(field.equals(cw.getFiled())){
                    return cw;
                }
            }
        }
        return null;
    }

}
