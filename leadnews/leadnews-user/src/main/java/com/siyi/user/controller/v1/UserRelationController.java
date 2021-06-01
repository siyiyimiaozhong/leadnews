package com.siyi.user.controller.v1;

import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.user.dtos.UserRelationDto;
import com.siyi.user.apis.UserRelationControllerApi;
import com.siyi.user.service.AppUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserRelationController implements UserRelationControllerApi {
    @Autowired
    private AppUserRelationService appUserRelationService;

    @Override
    @PostMapping("/user_follow")
    public ResponseResult follow(@RequestBody UserRelationDto dto){
        return appUserRelationService.follow(dto);
    }
}