package com.siyi.crawler.service.impl;

import com.siyi.crawler.service.CrawlerNewsCommentService;
import com.siyi.model.crawler.pojos.ClNewsComment;
import com.siyi.model.mappers.crawerls.ClNewsCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class CrawlerNewsCommentServiceImpl implements CrawlerNewsCommentService {

    @Autowired
    private ClNewsCommentMapper clNewsCommentMapper;

    @Override
    public void saveClNewsComment(ClNewsComment clNewsComment) {
        clNewsCommentMapper.insertSelective(clNewsComment);
    }
}
