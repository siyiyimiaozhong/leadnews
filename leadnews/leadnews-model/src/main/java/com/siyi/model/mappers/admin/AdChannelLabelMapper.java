package com.siyi.model.mappers.admin;

import com.siyi.model.admin.pojos.AdChannelLabel;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;

public interface AdChannelLabelMapper {
    
    int deleteByPrimaryKey(Integer id);

    int insert(AdChannelLabel record);

    int insertSelective(AdChannelLabel record);

    AdChannelLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdChannelLabel record);

    int updateByPrimaryKey(AdChannelLabel record);

    /**
     * 根据labelId查询
     * @param id
     * @return
     */
    AdChannelLabel selectByLabelId(Integer id);


}