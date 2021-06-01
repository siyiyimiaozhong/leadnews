package com.siyi.media.controller.v1;

import com.siyi.media.apis.AdChannelControllerApi;
import com.siyi.media.service.AdChannelService;
import com.siyi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController implements AdChannelControllerApi {
    @Autowired
    private AdChannelService channelService ;
    @Override
    @RequestMapping("/channels")
    public ResponseResult selectAll(){
        return ResponseResult.okResult(channelService.selectAll());
    }
}