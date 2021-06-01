package com.siyi.migration.service;

import com.siyi.model.article.pojos.ApArticleConfig;

import java.util.List;

public interface ApArticleConfigService {
    
    List<ApArticleConfig> queryByArticleIds(List<String> ids);

    ApArticleConfig getByArticleId(Integer id);
}