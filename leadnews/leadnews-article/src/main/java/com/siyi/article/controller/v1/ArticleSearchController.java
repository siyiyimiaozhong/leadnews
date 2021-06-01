package com.siyi.article.controller.v1;

import com.siyi.article.apis.ArticleSearchControllerApi;
import com.siyi.article.service.ApArticleSearchService;
import com.siyi.model.article.dtos.UserSearchDto;
import com.siyi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article/search")
public class ArticleSearchController implements ArticleSearchControllerApi {

    @Autowired
    private ApArticleSearchService apArticleSearchService;

    @PostMapping("/load_search_history")
    @Override
    public ResponseResult findUserSearch(@RequestBody UserSearchDto userSearchDto) {
    	return apArticleSearchService.findUserSearch(userSearchDto);
    }

    @PostMapping("/del_search")
    @Override
    public ResponseResult delUserSearch(@RequestBody UserSearchDto userSearchDto) {
        return apArticleSearchService.delUserSearch(userSearchDto);
    }

    @PostMapping("/clear_search")
    @Override
    public ResponseResult clearUserSearch(@RequestBody UserSearchDto userSearchDto) {
        return apArticleSearchService.clearUserSearch(userSearchDto);
    }

    @PostMapping("/load_hot_keywords")
    @Override
    public ResponseResult hotKeywords(@RequestBody UserSearchDto userSearchDto) {
        return apArticleSearchService.hotKeywords(userSearchDto.getHotDate());
    }

    @PostMapping("/associate_search")
    @Override
    public ResponseResult searchAssociate(@RequestBody UserSearchDto userSearchDto) {
        return apArticleSearchService.searchAssociate(userSearchDto);
    }

    @PostMapping("/article_search")
    @Override
    public ResponseResult esArticleSearch(@RequestBody UserSearchDto userSearchDto) {
        return apArticleSearchService.esArticleSearch(userSearchDto);
    }
}