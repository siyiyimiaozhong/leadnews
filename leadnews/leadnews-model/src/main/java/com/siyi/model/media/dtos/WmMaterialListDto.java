package com.siyi.model.media.dtos;

import com.siyi.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class WmMaterialListDto extends PageRequestDto {
    Short isCollected; //1 查询收藏的
}
