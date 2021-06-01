package com.siyi.article.service.impl;

import com.google.common.collect.Maps;
import com.siyi.article.service.AppArticleInfoService;
import com.siyi.model.article.dtos.ArticleInfoDto;
import com.siyi.model.article.pojos.ApArticleConfig;
import com.siyi.model.article.pojos.ApArticleContent;
import com.siyi.model.article.pojos.ApAuthor;
import com.siyi.model.article.pojos.ApCollection;
import com.siyi.model.behavior.pojos.ApBehaviorEntry;
import com.siyi.model.behavior.pojos.ApLikesBehavior;
import com.siyi.model.behavior.pojos.ApUnlikesBehavior;
import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.common.enums.AppHttpCodeEnum;
import com.siyi.model.crawler.core.parse.ZipUtils;
import com.siyi.model.mappers.app.*;
import com.siyi.model.user.pojos.ApUser;
import com.siyi.model.user.pojos.ApUserFollow;
import com.siyi.utils.common.BurstUtils;
import com.siyi.utils.threadlocal.AppThreadLocalUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Getter
@Service
@SuppressWarnings("all")
public class ApArticleInfoServiceImpl implements AppArticleInfoService {

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;
    @Autowired
    private ApCollectionMapper apCollectionMapper;
    @Autowired
    private ApUnlikesBehaviorMapper apUnlikesBehaviorMapper;
    @Autowired
    private ApLikesBehaviorMapper apLikesBehaviorMapper;
    @Autowired
    private ApAuthorMapper apAuthorMapper;
    @Autowired
    private ApUserFollowMapper apUserFollowMapper;

    /**
     * 加载文章详情内容
     * @param articleId
     * @return
     */
    public ResponseResult getArticleInfo(Integer articleId){
        // 参数无效
        if(null == articleId || articleId<1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApArticleConfig config = apArticleConfigMapper.selectByArticleId(articleId);
        Map<String,Object> data = new HashMap<>();
        // 参数无效
        if(null == config){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }else if(!config.getIsDelete()){
            // 没删除的标识才返回给客户端
            ApArticleContent apArticleContent = apArticleContentMapper.selectByArticleId(articleId);
            String content = ZipUtils.gunzip(apArticleContent.getContent());
            apArticleContent.setContent(content);
            data.put("content",apArticleContent);
        }
        data.put("config",config);
        return ResponseResult.okResult(data);
    }

    /**
     * 加载文章详情的初始化配置信息，比如关注、喜欢、不喜欢、阅读位置等
     * @param dto
     * @return
     */
    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto){
        ApUser user = AppThreadLocalUtils.getUser();
        // 用户和设备不能同时为空
        if(user==null&& dto.getEquipmentId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if(null != user){
            userId = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = this.apBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, dto.getEquipmentId());
        // 行为实体找以及注册了，逻辑上这里是必定有值得，除非参数错误
        if(apBehaviorEntry==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        boolean isUnLike=false,isLike=false,isCollection=false,isFollow=false;
        String burst = BurstUtils.groudOne(apBehaviorEntry.getId());

        // 判断是否是已经不喜欢
        ApUnlikesBehavior apUnlikesBehavior = this.apUnlikesBehaviorMapper.selectLastUnLike(apBehaviorEntry.getId(),dto.getArticleId());
        if(apUnlikesBehavior!=null && apUnlikesBehavior.getType()==ApUnlikesBehavior.Type.UNLIKE.getCode()){
            isUnLike=true;
        }
        // 判断是否是已经喜欢
        ApLikesBehavior apLikesBehavior = this.apLikesBehaviorMapper.selectLastLike(burst,apBehaviorEntry.getId(),dto.getArticleId(), ApCollection.Type.ARTICLE.getCode());
        if(apLikesBehavior!=null && apLikesBehavior.getOperation()==ApLikesBehavior.Operation.LIKE.getCode()){
            isLike=true;
        }
        // 判断是否收藏
        ApCollection apCollection = this.apCollectionMapper.selectForEntryId(burst,apBehaviorEntry.getId(),dto.getArticleId(),ApCollection.Type.ARTICLE.getCode());
        if(apCollection!=null){
            isCollection=true;
        }
        // 判断是否关注
        ApAuthor apAuthor = this.apAuthorMapper.selectById(dto.getAuthorId());
        if(user!=null && apAuthor!=null && apAuthor.getUserId()!=null) {
            ApUserFollow apUserFollow = this.apUserFollowMapper.selectByFollowId(BurstUtils.groudOne(user.getId()), user.getId(), apAuthor.getUserId().intValue());
            if (apUserFollow != null) {
                isFollow = true;
            }
        }

        Map<String,Object> data = Maps.newHashMap();
        data.put("isfollow",isFollow);
        data.put("islike",isLike);
        data.put("isunlike",isUnLike);
        data.put("iscollection",isCollection);

        return ResponseResult.okResult(data);
    }
}