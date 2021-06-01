package com.siyi.migration.job;

import com.siyi.common.quartz.AbstractJob;
import com.siyi.migration.service.ArticleQuantityService;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 全量数据从mysql 同步到HBase
 */
@Component
@DisallowConcurrentExecution
@Log4j2
public class MigrationDbToHBaseQuartz extends AbstractJob {

    @Autowired
    private ArticleQuantityService articleQuantityService;


    @Override
    public String[] triggerCron() {
        return new String[]{"0 0/5 * * * ?"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("开始进行数据库到HBASE同步任务");
        articleQuantityService.dbToHbase();
        log.info("数据库到HBASE同步任务完成");
    }

}