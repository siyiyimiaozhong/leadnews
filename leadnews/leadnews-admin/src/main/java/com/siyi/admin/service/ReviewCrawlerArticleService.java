package com.siyi.admin.service;

import com.siyi.model.crawler.pojos.ClNews;

public interface ReviewCrawlerArticleService {
    /**
     * 爬虫端发布文章审核
     */
    public void autoReviewArticleByCrawler() throws Exception;

    public void autoReviewArticleByCrawler(Integer clNewsId) throws Exception;

    public void autoReviewArticleByCrawler(ClNews clNews) throws Exception;
}