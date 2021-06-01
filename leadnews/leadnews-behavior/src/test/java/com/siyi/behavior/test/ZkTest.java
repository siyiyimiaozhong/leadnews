package com.siyi.behavior.test;

import com.siyi.behavior.BehaviorJarApplication;
import com.siyi.common.zookeeper.sequence.Sequences;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = BehaviorJarApplication.class)
@RunWith(SpringRunner.class)
public class ZkTest {

    @Autowired
    private Sequences sequences;

    @Test
    public void test1(){
        Long aLong = sequences.sequenceApReadBehavior();
        System.out.println(aLong + " -------------");
    }
}
