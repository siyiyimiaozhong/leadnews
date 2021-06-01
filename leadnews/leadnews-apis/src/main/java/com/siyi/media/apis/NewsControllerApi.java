package com.siyi.media.apis;

import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.media.dtos.WmNewsDto;
import com.siyi.model.media.dtos.WmNewsPageReqDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface NewsControllerApi {

   /**  
    * 提交文章*  
    * @param wmNews*  
    * @return*  
    */  
   ResponseResult summitNews(WmNewsDto wmNews);

    /**  
	* 保存草稿
	* @param wmNews
	* @return
	*/ 
    ResponseResult saveDraftNews(WmNewsDto wmNews);

    /**
     * 用户查询文章列表
     * @return
     */
    ResponseResult listByUser(WmNewsPageReqDto dto);

    /**
     * 根据id获取文章信息
     * @param wmNews
     * @return
     */
    ResponseResult wmNews(WmNewsDto wmNews);

    /**
     * 删除文章
     * @param wmNews
     * @return
     */
    ResponseResult delNews(WmNewsDto wmNews);
}