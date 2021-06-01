package com.siyi.model.mappers.wemedia;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface WmNewsMaterialMapper {
	int countByMid(Integer mid);

	int delByNewsId(@Param("nid") Integer nid);

	void saveRelationsByContent(@Param("materials") Map<String, Object> materials,@Param("newsId") Integer newsId,@Param("type") Short type);
}