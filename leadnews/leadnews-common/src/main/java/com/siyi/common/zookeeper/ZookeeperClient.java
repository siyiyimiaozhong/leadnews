package com.siyi.common.zookeeper;

import com.google.common.collect.Maps;
import com.siyi.common.zookeeper.sequence.ZkSequence;
import com.siyi.common.zookeeper.sequence.ZkSequenceEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Map;

@Getter
@Setter
public class ZookeeperClient {
    private static Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

    private String host;
    private String sequencePath;

    // 重试休眠时间
    private final int SLEEP_TIME_MS = 1000;
    // 最大重试1000次
    private final int MAX_RETRIES = 1000;
    //会话超时时间
    private final int SESSION_TIMEOUT = 30 * 1000;
    //连接超时时间
    private final int CONNECTION_TIMEOUT = 3 * 1000;

    //创建连接实例
    private CuratorFramework client = null;
    // 序列化集合
    private Map<String, ZkSequence> zkSequence = Maps.newConcurrentMap();

    public ZookeeperClient(String host, String sequencePath){
        this.host = host;
        this.sequencePath = sequencePath;
    }

    @PostConstruct
    public void init(){
        this.client = CuratorFrameworkFactory.builder().connectString(this.host)
                .connectionTimeoutMs(this.CONNECTION_TIMEOUT)
                .sessionTimeoutMs(this.SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(this.SLEEP_TIME_MS,this.MAX_RETRIES))
                .build();
        this.client.start();
        this.initZkSequence();
    }

    public void initZkSequence(){
        ZkSequenceEnum[] list = ZkSequenceEnum.values();
        for (int i = 0; i < list.length; i++) {
            String name = list[i].name();
            String path = this.sequencePath+name;
            ZkSequence seq = new ZkSequence(path,this.client);
            zkSequence.put(name,seq);
        }
    }

    /**
     * 生成SEQ
     * @param name
     * @return
     * @throws Exception
     */
    public Long sequence(ZkSequenceEnum name){
        try {
            ZkSequence seq = zkSequence.get(name.name());
            if (seq != null) {
                return seq.sequence();
            }
        }catch (Exception e){
            logger.error("获取[{}]Sequence错误:{}",name,e);
            e.printStackTrace();
        }
        return null;
    }
}
