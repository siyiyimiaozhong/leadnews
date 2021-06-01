package com.siyi.media.apis;

import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.media.dtos.WmMaterialDto;
import com.siyi.model.media.dtos.WmMaterialListDto;
import org.springframework.web.multipart.MultipartFile;

public interface MaterialManageControllerApi {
	/**  
	* 上传图片
	* @param multipartFile
	* @return
	*/ 
	ResponseResult uploadPicture(MultipartFile multipartFile);

	/**
	 * 删除图片
	 * @param wmMaterial
	 * @return
	 */
	ResponseResult delPicture(WmMaterialDto wmMaterial);

	/**
	 * 分页查询
	 * @param dto
	 * @return
	 */
	ResponseResult list(WmMaterialListDto dto);

	/**
	 * 收藏
	 * @param dto
	 * @return
	 */
	ResponseResult collectionMaterial(WmMaterialDto dto);

	/**
	 * 取消收藏
	 * @param dto
	 * @return
	 */
	ResponseResult cancleCollectionMaterial(WmMaterialDto dto);
}