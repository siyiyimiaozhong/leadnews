package com.siyi.media.service.impl;

import com.google.common.collect.Maps;
import com.siyi.media.service.UserLoginService;
import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.common.enums.AppHttpCodeEnum;
import com.siyi.model.mappers.wemedia.WmUserMapper;
import com.siyi.model.media.pojos.WmUser;
import com.siyi.utils.jwt.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@SuppressWarnings("all")
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private WmUserMapper wmUserMapper;

    public ResponseResult login(WmUser user){
        if (StringUtils.isEmpty(user.getName()) && StringUtils.isEmpty(user.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE,"用户名和密码不能为空");
        }
        WmUser wmUser = wmUserMapper.selectByName(user.getName());
        if(wmUser!=null){
            if(user.getPassword().equals(wmUser.getPassword())){
                Map<String,Object> map = Maps.newHashMap();
                wmUser.setPassword("");
                wmUser.setSalt("");
                map.put("token", AppJwtUtil.getToken(wmUser));
                map.put("user",wmUser);
                return ResponseResult.okResult(map);
            }else{
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
        }else{
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
        }
    }
}