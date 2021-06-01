package com.siyi.behavior.test;

import com.siyi.behavior.service.AppLikesBehaviorService;
import com.siyi.behavior.service.AppShowBehaviorService;
import com.siyi.model.article.pojos.ApArticle;
import com.siyi.model.behavior.dtos.LikesBehaviorDto;
import com.siyi.model.behavior.dtos.ShowBehaviorDto;
import com.siyi.model.user.pojos.ApUser;
import com.siyi.utils.threadlocal.AppThreadLocalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BehaviorTest {
    @Autowired
    private AppShowBehaviorService appShowBehaviorService;
    @Autowired
    private AppLikesBehaviorService appLikesBehaviorService;

    @Test
    public void testSave(){
        ApUser user = new ApUser();
        user.setId(1L);
        AppThreadLocalUtils.setUser(user);
        ShowBehaviorDto dto = new ShowBehaviorDto();
        List<ApArticle> list = new ArrayList<>();
        ApArticle apArticle = new ApArticle();
        apArticle.setId(10130);
        list.add(apArticle);
        dto.setArticleIds(list);
        appShowBehaviorService.saveShowBehavior(dto);
    }

    @Test
    public void testLikesSave(){
        ApUser user = new ApUser();
        user.setId(1l);
        AppThreadLocalUtils.setUser(user);
        LikesBehaviorDto dto = new LikesBehaviorDto();
        dto.setEntryId(10116);
        dto.setOperation((short)0);
        dto.setType((short)0);
        appLikesBehaviorService.saveLikesBehavior(dto);
    }
}
