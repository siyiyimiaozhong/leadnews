package com.siyi.migration.service.impl;

import com.siyi.migration.service.ApArticleConfigService;
import com.siyi.model.article.pojos.ApArticleConfig;
import com.siyi.model.mappers.app.ApArticleConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("all")
public class ApArticleConfigServiceImpl implements ApArticleConfigService {

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Override
    public List<ApArticleConfig> queryByArticleIds(List<String> ids) {
        return apArticleConfigMapper.selectByArticleIds(ids);
    }

    @Override
    public ApArticleConfig getByArticleId(Integer id) {
        return apArticleConfigMapper.selectByArticleId(id);
    }
}
