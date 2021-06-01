package com.siyi.migration.service.impl;

import com.siyi.migration.service.ApArticleService;
import com.siyi.model.article.pojos.ApArticle;
import com.siyi.model.mappers.app.ApArticleMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class ApArticleServiceImpl implements ApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;


    public ApArticle getById(Long id) {
        return apArticleMapper.selectById(id);
    }

    /**
     * 获取未同步的数据
     *
     * @return
     */
    public List<ApArticle> getUnsyncApArticleList() {
        ApArticle apArticleQuery = new ApArticle();
        apArticleQuery.setSyncStatus(false);
        return apArticleMapper.selectList(apArticleQuery);
    }

    /**
     * 更新数据同步状态
     *
     * @param apArticle
     */

    public void updateSyncStatus(ApArticle apArticle) {
        log.info("开始更新数据同步状态，apArticle：{}", apArticle);
        if (null != apArticle) {
            apArticle.setSyncStatus(true);
            apArticleMapper.updateSyncStatus(apArticle);
        }
    }

}