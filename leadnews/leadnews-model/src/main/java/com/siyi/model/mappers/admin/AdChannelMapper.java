package com.siyi.model.mappers.admin;

import com.siyi.model.admin.pojos.AdChannel;

import java.util.List;

public interface AdChannelMapper {
    /**
     * 查询所有
     */
    public List<AdChannel> selectAll();

    /**
     * 查询频道
     * @param id
     * @return
     */
    AdChannel selectByPrimaryKey(Integer id);
}