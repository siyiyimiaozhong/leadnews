package com.siyi.article.controller.v1;

import com.siyi.article.apis.ArticleInfoControllerApi;
import com.siyi.article.service.AppArticleInfoService;
import com.siyi.model.article.dtos.ArticleInfoDto;
import com.siyi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleInfoController implements ArticleInfoControllerApi {

    @Autowired
    private AppArticleInfoService appArticleInfoService;

    @Override
    @PostMapping("/load_article_info")
    public ResponseResult loadArticleInfo(@RequestBody ArticleInfoDto dto) {
        return appArticleInfoService.getArticleInfo(dto.getArticleId());
    }

    @Override
    @PostMapping("/load_article_behavior")
    public ResponseResult loadArticleBehavior(@RequestBody ArticleInfoDto dto) {
        return appArticleInfoService.loadArticleBehavior(dto);
    }
}