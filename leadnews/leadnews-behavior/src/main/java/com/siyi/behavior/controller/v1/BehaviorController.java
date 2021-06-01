package com.siyi.behavior.controller.v1;

import com.siyi.behavior.apis.BehaviorControllerApi;
import com.siyi.behavior.service.AppLikesBehaviorService;
import com.siyi.behavior.service.AppReadBehaviorService;
import com.siyi.behavior.service.AppShowBehaviorService;
import com.siyi.behavior.service.AppUnLikesBehaviorService;
import com.siyi.model.behavior.dtos.LikesBehaviorDto;
import com.siyi.model.behavior.dtos.ReadBehaviorDto;
import com.siyi.model.behavior.dtos.ShowBehaviorDto;
import com.siyi.model.behavior.dtos.UnLikesBehaviorDto;
import com.siyi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/behavior")
@SuppressWarnings("all")
public class BehaviorController implements BehaviorControllerApi {

    @Autowired
    private AppShowBehaviorService appShowBehaviorService;
    @Autowired
    private AppLikesBehaviorService appLikesBehaviorService;
    @Autowired
    private AppUnLikesBehaviorService appUnLikesBehaviorService;
    @Autowired
    private AppReadBehaviorService appReadBehaviorService;

    @Override
    @RequestMapping("/show_behavior")
    public ResponseResult saveShowBehavior(@RequestBody ShowBehaviorDto dto) {
        return appShowBehaviorService.saveShowBehavior(dto);
    }

    @Override
    @PostMapping("/like_behavior")
    public ResponseResult saveLikesBehavior(@RequestBody LikesBehaviorDto dto) {
        return appLikesBehaviorService.saveLikesBehavior(dto);
    }

    @Override
    @PostMapping("/unlike_behavior")
    public ResponseResult saveUnLikesBehavior(@RequestBody UnLikesBehaviorDto dto) {
        return appUnLikesBehaviorService.saveUnLikesBehavior(dto);
    }

    @Override
    @PostMapping("/read_behavior")
    public ResponseResult saveReadBehavior(@RequestBody ReadBehaviorDto dto) {
        return appReadBehaviorService.saveReadBehavior(dto);
    }
}