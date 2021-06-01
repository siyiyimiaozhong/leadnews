package com.siyi.model.mappers.wemedia;

import com.siyi.model.media.dtos.WmNewsPageReqDto;
import com.siyi.model.media.pojos.WmNews;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WmNewsMapper {
    
	/**
     * 根据主键修改
     * @param record
     * @return
     */
    int updateByPrimaryKey(WmNews record);

    /**
     * 添加草稿新闻
     * @param dto
     * @return
     */
    int insertNewsForEdit(WmNews dto);

    /**
     * 查询根据dto条件
     * @param dto
     * @param uid
     * @return
     */
    List<WmNews> selectBySelective(@Param("dto") WmNewsPageReqDto dto,@Param("uid") Long uid);

    /**
     * 查询总数统计
     * @param dto
     * @param uid
     * @return
     */
    int countSelectBySelective(@Param("dto") WmNewsPageReqDto dto,@Param("uid") Long uid);

    /**
     * 根据id查询文章
     * @param id
     * @return
     */
    WmNews selectNewsDetailByPrimaryKey(@Param("id") Integer id);

    /**
     * 根据id删除文章
     * @param id
     * @return
     */
    int deleteByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(WmNews record);
}