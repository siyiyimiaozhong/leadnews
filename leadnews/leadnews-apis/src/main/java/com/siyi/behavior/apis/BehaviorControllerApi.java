package com.siyi.behavior.apis;

import com.siyi.model.behavior.dtos.LikesBehaviorDto;
import com.siyi.model.behavior.dtos.ReadBehaviorDto;
import com.siyi.model.behavior.dtos.ShowBehaviorDto;
import com.siyi.model.behavior.dtos.UnLikesBehaviorDto;
import com.siyi.model.common.dtos.ResponseResult;

public interface BehaviorControllerApi {
    /**
     * 保存用户点击文章的行为
     *
     * @param dto
     * @return
     */
    ResponseResult saveShowBehavior(ShowBehaviorDto dto);

    /**
     * 保存点赞行为
     *
     * @param dto
     * @return
     */
    ResponseResult saveLikesBehavior(LikesBehaviorDto dto);

    /**
     * 保存不喜欢行为
     *
     * @param dto
     * @return
     */
    ResponseResult saveUnLikesBehavior(UnLikesBehaviorDto dto);

    /**
     * 保存或修改阅读行为信息
     *
     * @param dto
     * @return
     */
    ResponseResult saveReadBehavior(ReadBehaviorDto dto);
}
