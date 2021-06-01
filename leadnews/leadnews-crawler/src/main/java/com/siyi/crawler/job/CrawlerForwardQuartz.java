package com.siyi.crawler.job;

import com.siyi.common.quartz.AbstractJob;
import com.siyi.crawler.manager.ProcessingFlowManager;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution //当前任务不允许同时执行
@Log4j2
public class CrawlerForwardQuartz extends AbstractJob {

    @Autowired
    private ProcessingFlowManager processingFlowManager;

    @Override
    public String[] triggerCron() {
        return new String[]{"* 0/30 * * * ?"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("开始正向抓取");
//        processingFlowManager.handel();
        log.info("正向抓取结束，耗时:{}",System.currentTimeMillis()-currentTimeMillis);
    }
}
