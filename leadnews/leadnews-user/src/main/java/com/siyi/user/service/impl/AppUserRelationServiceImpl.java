package com.siyi.user.service.impl;

import com.siyi.common.zookeeper.sequence.Sequences;
import com.siyi.model.article.pojos.ApAuthor;
import com.siyi.model.behavior.dtos.FollowBehaviorDto;
import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.common.enums.AppHttpCodeEnum;
import com.siyi.model.mappers.app.ApAuthorMapper;
import com.siyi.model.mappers.app.ApUserFanMapper;
import com.siyi.model.mappers.app.ApUserFollowMapper;
import com.siyi.model.mappers.app.ApUserMapper;
import com.siyi.model.user.dtos.UserRelationDto;
import com.siyi.model.user.pojos.ApUser;
import com.siyi.model.user.pojos.ApUserFan;
import com.siyi.model.user.pojos.ApUserFollow;
import com.siyi.user.service.AppFollowBehaviorService;
import com.siyi.user.service.AppUserRelationService;
import com.siyi.utils.common.BurstUtils;
import com.siyi.utils.threadlocal.AppThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class AppUserRelationServiceImpl implements AppUserRelationService {

    private final Logger logger = LoggerFactory.getLogger(AppUserRelationServiceImpl.class);

    @Autowired
    private ApUserFollowMapper apUserFollowMapper;
    @Autowired
    private ApUserFanMapper apUserFanMapper;
    @Autowired
    private ApAuthorMapper apAuthorMapper;
    @Autowired
    private ApUserMapper apUserMapper;
    @Autowired
    private AppFollowBehaviorService appFollowBehaviorService;
    @Autowired
    private Sequences sequences;

    /**
     * 关注/取消一个人
     *
     * @param dto
     * @return
     */
    public ResponseResult follow(UserRelationDto dto) {
        if (dto.getOperation() == null || dto.getOperation() < 0 || dto.getOperation() > 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "无效的operation参数");
        }
        Integer followId = dto.getUserId();
        if (followId == null && dto.getAuthorId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "followId或authorId不能为空");
        } else if (followId == null) {
            ApAuthor apAuthor = apAuthorMapper.selectById(dto.getAuthorId());
            if (apAuthor != null) {
                followId = apAuthor.getUserId().intValue();
            }
        }
        if (followId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "关注人不存在");
        } else {
            ApUser user = AppThreadLocalUtils.getUser();
            if (user != null) {
                if (dto.getOperation() == 0) {
                    return followByUserId(user, followId, dto.getArticleId());
                } else {
                    return followCancelByUserId(user, followId);
                }
            } else {
                return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            }
        }
    }

    /**
     * 处理关注逻辑
     *
     * @param user
     * @param followId
     * @return
     */
    private ResponseResult followByUserId(ApUser user, Integer followId, Integer articleId) {
        // 判断用户是否存在
        ApUser followUser = apUserMapper.selectById(followId);
        if (followUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "关注用户不存在");
        }
        ApUserFollow auf = apUserFollowMapper.selectByFollowId(BurstUtils.groudOne(user.getId()), user.getId(), followId);
        if (auf == null) {
            ApUserFan fan = apUserFanMapper.selectByFansId(BurstUtils.groudOne(followId), followId, user.getId());
            if (fan == null) {
                fan = new ApUserFan();
                fan.setId(sequences.sequenceApUserFan());
                fan.setUserId(followId);
                fan.setFansId(user.getId());
                fan.setFansName(user.getName());
                fan.setLevel((short) 0);
                fan.setIsDisplay(true);
                fan.setIsShieldComment(false);
                fan.setIsShieldLetter(false);
                fan.setBurst(BurstUtils.encrypt(fan.getId(), fan.getUserId()));
                apUserFanMapper.insert(fan);
            }
            auf = new ApUserFollow();
            auf.setId(sequences.sequenceApUserFollow());
            auf.setUserId(user.getId());
            auf.setFollowId(followId);
            auf.setFollowName(followUser.getName());
            auf.setCreatedTime(new Date());
            auf.setLevel((short) 0);
            auf.setIsNotice(true);
            auf.setBurst(BurstUtils.encrypt(auf.getId(), auf.getUserId()));
            // 记录关注行为
            FollowBehaviorDto dto = new FollowBehaviorDto();
            dto.setFollowId(followId);
            dto.setArticleId(articleId);
            appFollowBehaviorService.saveFollowBehavior(dto);
            return ResponseResult.okResult(apUserFollowMapper.insert(auf));
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "已关注");
        }
    }

    /**
     * 处理取消关注逻辑
     *
     * @param user
     * @param followId
     * @return
     */
    private ResponseResult followCancelByUserId(ApUser user, Integer followId) {
        ApUserFollow auf = apUserFollowMapper.selectByFollowId(BurstUtils.groudOne(user.getId()), user.getId(), followId);
        if (auf == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "未关注");
        } else {
            ApUserFan fan = apUserFanMapper.selectByFansId(BurstUtils.groudOne(followId), followId, user.getId());
            if (fan != null) {
                apUserFanMapper.deleteByFansId(BurstUtils.groudOne(followId), followId, user.getId());
            }
            return ResponseResult.okResult(apUserFollowMapper.deleteByFollowId(BurstUtils.groudOne(user.getId()), user.getId(), followId));
        }
    }
}