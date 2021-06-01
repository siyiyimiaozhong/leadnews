package com.siyi.crawler.test;

import com.siyi.crawler.process.entity.ProcessFlowData;
import com.siyi.crawler.process.original.impl.CsdnOriginalDataProcess;
import com.siyi.model.crawler.core.parse.ParseItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CsdnOriginalDataProcessTest {
    @Autowired
    private CsdnOriginalDataProcess csdnOriginalDataProcess;

    @Test
    public void test(){
        List<ParseItem> parseItems = csdnOriginalDataProcess.parseOriginalRequestData(new ProcessFlowData());
        parseItems.forEach(System.out::println);
    }
}