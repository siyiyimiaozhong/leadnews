package com.siyi.article.controller.v1;

import com.siyi.article.apis.ArticleHomeControllerApi;
import com.siyi.article.service.AppArticleService;
import com.siyi.common.article.constans.ArticleConstans;
import com.siyi.model.article.dtos.ArticleHomeDto;
import com.siyi.model.article.pojos.ApArticle;
import com.siyi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/article")
public class ArticleHomeController implements ArticleHomeControllerApi {

    @Autowired
    private AppArticleService appArticleService;

    @Override
    @PostMapping("load")
    public ResponseResult load(ArticleHomeDto dto) {
        List<ApArticle> apArticles = appArticleService.load(dto, ArticleConstans.LOADTYPE_LOAD_MORE);
        return ResponseResult.okResult(apArticles);
    }

    @Override
    @PostMapping("loadmore")
    public ResponseResult loadMore(ArticleHomeDto dto) {
        List<ApArticle> apArticles = appArticleService.load(dto,ArticleConstans.LOADTYPE_LOAD_MORE);
        return ResponseResult.okResult(apArticles);
    }

    @Override
    @PostMapping("loadnew")
    public ResponseResult loadNew(ArticleHomeDto dto) {
        List<ApArticle> apArticles = appArticleService.load(dto,ArticleConstans.LOADTYPE_LOAD_NEW);
        return ResponseResult.okResult(apArticles);
    }

    @RequestMapping("demo")
    public String demo(){
        return "hello";
    }
}
