package com.siyi.migration.service.impl;

import com.siyi.migration.service.ApAuthorService;
import com.siyi.model.article.pojos.ApAuthor;
import com.siyi.model.mappers.app.ApAuthorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApAuthorServiceImpl implements ApAuthorService {

    @Autowired
    private ApAuthorMapper apAuthorMapper;

    @Override
    public List<ApAuthor> queryByIds(List<Integer> ids) {
        return apAuthorMapper.selectByIds(ids);
    }

    @Override
    public ApAuthor getById(Long id) {
        if (null != id) {
            return apAuthorMapper.selectById(id.intValue());
        }
        return null;

    }
}