package com.siyi.article.controller.v2;

import com.siyi.article.apis.ArticleHomeControllerApi;
import com.siyi.article.service.AppArticleService;
import com.siyi.common.article.constans.ArticleConstans;
import com.siyi.model.article.dtos.ArticleHomeDto;
import com.siyi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/article")
public class ArticleHomeV2Controller implements ArticleHomeControllerApi {

    @Autowired
    private AppArticleService appArticleService;

    @Override
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto) {
        return appArticleService.loadV2( ArticleConstans.LOADTYPE_LOAD_MORE, dto, true);
    }

    @Override
    @PostMapping("/loadmore")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
        return appArticleService.loadV2( ArticleConstans.LOADTYPE_LOAD_MORE, dto, false);
    }

    @Override
    @PostMapping("/loadnew")
    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto) {
        return appArticleService.loadV2( ArticleConstans.LOADTYPE_LOAD_NEW, dto, false);
    }

}