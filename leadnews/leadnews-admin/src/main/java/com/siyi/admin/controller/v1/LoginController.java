package com.siyi.admin.controller.v1;

import com.siyi.admin.apis.LoginControllerApi;
import com.siyi.admin.service.UserLoginService;
import com.siyi.model.admin.pojos.AdUser;
import com.siyi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController implements LoginControllerApi {

    @Autowired
    private UserLoginService userLoginService ;

    @RequestMapping("/in")
    @Override
    public ResponseResult login(@RequestBody AdUser user){
        return userLoginService.login(user);
    }

}