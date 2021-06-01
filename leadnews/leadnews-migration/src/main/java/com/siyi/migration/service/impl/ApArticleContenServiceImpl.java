package com.siyi.migration.service.impl;

import com.siyi.migration.service.ApArticleContenService;
import com.siyi.model.article.pojos.ApArticleContent;
import com.siyi.model.mappers.app.ApArticleContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("all")
public class ApArticleContenServiceImpl implements ApArticleContenService {

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Override
    public List<ApArticleContent> queryByArticleIds(List<String> ids) {
        return apArticleContentMapper.selectByArticleIds(ids);
    }

    @Override
    public ApArticleContent getByArticleIds(Integer id) {
        return apArticleContentMapper.selectByArticleId(id);
    }
}
