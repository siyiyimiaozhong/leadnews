package com.siyi.article.test;

import com.siyi.article.ArticleJarApplication;
import com.siyi.article.service.AppArticleService;
import com.siyi.common.article.constans.ArticleConstans;
import com.siyi.model.article.pojos.ApArticle;
import com.siyi.model.user.pojos.ApUser;
import com.siyi.utils.threadlocal.AppThreadLocalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = ArticleJarApplication.class)
@RunWith(SpringRunner.class)
public class ArticleTest {
    @Autowired
    private AppArticleService appArticleService;

    @Test
    public void testArticle(){
        ApUser apUser = new ApUser();
        apUser.setId(2104L);
        AppThreadLocalUtils.setUser(apUser);
        List<ApArticle> load = appArticleService.load(null, ArticleConstans.LOADTYPE_LOAD_MORE);
        load.forEach(System.out::println);
    }
}
