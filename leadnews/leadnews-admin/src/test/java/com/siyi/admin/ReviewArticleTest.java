package com.siyi.admin;

import com.siyi.admin.service.ReviewCrawlerArticleService;
import com.siyi.admin.service.ReviewMediaArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReviewArticleTest {

    @Autowired
    private ReviewMediaArticleService reviewMediaArticleService;
    @Autowired
    private ReviewCrawlerArticleService reviewCrawlerArticleService;

    @Test
    public void testReview(){
        reviewMediaArticleService.autoReviewArticleByMedia(6101);
    }

    @Test
    public void testReviewCrawler() throws Exception {
        reviewCrawlerArticleService.autoReviewArticleByCrawler(32104);
    }
}