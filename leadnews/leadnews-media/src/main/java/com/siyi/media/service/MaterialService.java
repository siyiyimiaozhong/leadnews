package com.siyi.media.service;

import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.media.dtos.WmMaterialDto;
import com.siyi.model.media.dtos.WmMaterialListDto;
import org.springframework.web.multipart.MultipartFile;

public interface MaterialService {
	/**  
	* 上传图片接口*  
	* @param multipartFile*  
	* @return*  
	*/  
	ResponseResult uploadPicture(MultipartFile multipartFile);

	/**
	 * 删除图片
	 * @param dto
	 * @return
	 */
	ResponseResult delPicture(WmMaterialDto dto);

	/**
	 * 分页查询列表
	 * @param dto
	 * @return
	 */
	ResponseResult findList(WmMaterialListDto dto);

	/**
	 * 收藏或取消收藏
	 * @param dto
	 * @param type
	 * @return
	 */
	ResponseResult changeUserMaterialStatus(WmMaterialDto dto, Short type);
}