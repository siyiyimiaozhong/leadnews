package com.siyi.crawler.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AdLabelServiceTest {

    @Autowired
    private AdLabelService adLabelService;

    @Test
    public void testGetLabelIds(){
        String labelIds = adLabelService.getLabelIds("java,docker,8888xxxx,6667");
        System.out.println(labelIds);
    }

    @Test
    public void testGetAdChannelByLabelIds(){
        Integer adChannelByLabelIds = adLabelService.getAdChannelByLabelIds("9");
        System.out.println(adChannelByLabelIds);
    }
}
