package com.siyi.migration.service;

import com.siyi.model.article.pojos.ApArticleContent;

import java.util.List;

public interface ApArticleContenService {

    List<ApArticleContent> queryByArticleIds(List<String> ids);


    ApArticleContent getByArticleIds(Integer id);
}
