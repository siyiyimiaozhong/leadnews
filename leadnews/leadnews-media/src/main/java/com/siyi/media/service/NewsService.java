package com.siyi.media.service;

import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.media.dtos.WmNewsDto;
import com.siyi.model.media.dtos.WmNewsPageReqDto;

public interface NewsService {
	/**  
	* 自媒体发布文章 
	* @param wmNews
	* @return
	*/  
	ResponseResult saveNews(WmNewsDto wmNews, Short type);

	/**
	 * 查询发布库中当前用户文章信息
	 * @param dto
	 * @return
	 */
	ResponseResult listByUser(WmNewsPageReqDto dto);

	/**
	 * 根据文章id查询文章
	 * @return
	 */
	ResponseResult findWmNewsById(WmNewsDto wmNews);

	/**
	 * 删除文章
	 * @param wmNews
	 * @return
	 */
	ResponseResult delNews(WmNewsDto wmNews);
}