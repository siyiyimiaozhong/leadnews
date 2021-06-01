package com.siyi.media.controller.v1;

import com.siyi.media.apis.LoginControllerApi;
import com.siyi.media.service.UserLoginService;
import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.media.pojos.WmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController implements LoginControllerApi {

    @Autowired
    private UserLoginService userLoginService ;

    @Override
    @RequestMapping("/in")
    public ResponseResult login(@RequestBody WmUser user){
        return userLoginService.login(user);
    }
}