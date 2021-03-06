package com.siyi.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.siyi.article.service.ApArticleSearchService;
import com.siyi.article.utils.Trie;
import com.siyi.common.article.constans.ESIndexConstants;
import com.siyi.model.article.dtos.UserSearchDto;
import com.siyi.model.article.pojos.ApArticle;
import com.siyi.model.article.pojos.ApAssociateWords;
import com.siyi.model.article.pojos.ApHotWords;
import com.siyi.model.behavior.pojos.ApBehaviorEntry;
import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.common.enums.AppHttpCodeEnum;
import com.siyi.model.mappers.app.*;
import com.siyi.model.user.pojos.ApUser;
import com.siyi.model.user.pojos.ApUserSearch;
import com.siyi.utils.threadlocal.AppThreadLocalUtils;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class ApArticleSearchServiceImpl implements ApArticleSearchService {
    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;
    @Autowired
    private ApUserSearchMapper apUserSearchMapper;
    @Autowired
    private ApHotWordsMapper apHotWordsMapper;
    @Autowired
    private ApAssociateWordsMapper apAssociateWordsMapper;
    @Autowired
    private JestClient jestClient;
    @Autowired
    private ApArticleMapper apArticleMapper;

    public ResponseResult getEntryId(UserSearchDto dto) {
        ApUser user = AppThreadLocalUtils.getUser();
        // ?????????????????????????????????
        if (user == null && dto.getEquipmentId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, dto.getEquipmentId());
        // ???????????????????????????????????????????????????????????????????????????????????????
        if (apBehaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return ResponseResult.okResult(apBehaviorEntry.getId());
    }

    @Override
    public ResponseResult findUserSearch(UserSearchDto dto) {
        if (dto.getPageSize() > 50 || dto.getPageSize() < 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ResponseResult ret = getEntryId(dto);
        if (ret.getCode() != AppHttpCodeEnum.SUCCESS.getCode()) {
            return ret;
        }
        List<ApUserSearch> list = apUserSearchMapper.selectByEntryId((Integer) ret.getData(), dto.getPageSize());
        return ResponseResult.okResult(list);
    }

    @Override
    public ResponseResult delUserSearch(UserSearchDto dto) {
        if(dto.getHisList() == null || dto.getHisList().size()<=0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        ResponseResult ret = getEntryId(dto);
        if(ret.getCode()!=AppHttpCodeEnum.SUCCESS.getCode()){
            return ret;
        }
        List<Integer> ids = dto.getHisList().stream().map(r -> r.getId()).collect(Collectors.toList());
        int rows = apUserSearchMapper.delUserSearch((Integer) ret.getData(),ids);
        return ResponseResult.okResult(rows);
    }

    @Override
    public ResponseResult clearUserSearch(UserSearchDto dto) {
        ResponseResult ret = getEntryId(dto);
        if(ret.getCode()!=AppHttpCodeEnum.SUCCESS.getCode()){
            return ret;
        }
        int rows = apUserSearchMapper.clearUserSearch((Integer) ret.getData());
        return ResponseResult.okResult(rows);
    }

    @Override
    public ResponseResult hotKeywords(String date) {
        if(StringUtils.isEmpty(date)){
            date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        }
        List<ApHotWords> list = apHotWordsMapper.queryByHotDate(date);
        return ResponseResult.okResult(list);
    }

    @Override
    public ResponseResult searchAssociate(UserSearchDto dto) {
        if(dto.getPageSize() > 50 || dto.getPageSize() < 1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        List<ApAssociateWords> aw = apAssociateWordsMapper.selectByAssociateWords("%"+dto.getSearchWords()+"%", dto.getPageSize());
        return ResponseResult.okResult(aw);
    }

    @Override
    public ResponseResult saveUserSearch(Integer entryId, String searchWords) {
        //?????????????????????????????????
        int count = apUserSearchMapper.checkExist(entryId, searchWords);
        if(count>0){
            return ResponseResult.okResult(1);
        }
        ApUserSearch apUserSearch = new ApUserSearch();
        apUserSearch.setEntryId(entryId);
        apUserSearch.setKeyword(searchWords);
        apUserSearch.setStatus(1);
        apUserSearch.setCreatedTime(new Date());
        int row = apUserSearchMapper.insert(apUserSearch);
        return ResponseResult.okResult(row);
    }

    @Override
    public ResponseResult esArticleSearch(UserSearchDto dto) {
        //????????????????????????
        //?????????????????????????????????
        if(dto.getFromIndex()==0){
            ResponseResult result = getEntryId(dto);
            if(result.getCode()!=AppHttpCodeEnum.SUCCESS.getCode()){
                return result;
            }
            this.saveUserSearch((int)result.getData(),dto.getSearchWords());
        }
        //??????????????????????????????
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("title",dto.getSearchWords()));
        //????????????
        searchSourceBuilder.from(dto.getFromIndex());
        searchSourceBuilder.size(dto.getPageSize());
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ESIndexConstants.ARTICLE_INDEX).addType(ESIndexConstants.DEFAULT_DOC).build();
        try {
            SearchResult searchResult = jestClient.execute(search);
            List<ApArticle> sourceAsObjectList = searchResult.getSourceAsObjectList(ApArticle.class);
            List<ApArticle> resultList = new ArrayList<>();
            for (ApArticle apArticle : sourceAsObjectList) {
                apArticle = apArticleMapper.selectById(Long.valueOf(apArticle.getId()));
                if(apArticle==null){
                    continue;
                }
                resultList.add(apArticle);
            }
            return ResponseResult.okResult(resultList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ResponseResult searchAssociateV2(UserSearchDto dto) {
        if(dto.getPageSize()>50){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String assoStr = redisTemplate.opsForValue().get("associate_list");
        List<ApAssociateWords> aw = null;
        if(StringUtils.isNotEmpty(assoStr)){
            aw = JSON.parseArray(assoStr, ApAssociateWords.class);
        }else{
            aw = apAssociateWordsMapper.selectAllAssociateWords();
            redisTemplate.opsForValue().set("associate_list", JSON.toJSONString(aw));
        }
        //needed cache trie
        Trie t = new Trie();
        for (ApAssociateWords a : aw){
            t.insert(a.getAssociateWords());
        }
        List<String> ret = t.startWith(dto.getSearchWords());
        List<ApAssociateWords> wrapperList = Lists.newArrayList();
        for(String s : ret){
            ApAssociateWords apAssociateWords = new ApAssociateWords();
            apAssociateWords.setAssociateWords(s);
            wrapperList.add(apAssociateWords);
        }
        return ResponseResult.okResult(wrapperList);
    }
}
