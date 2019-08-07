package com.crazy.portal.task.postgre.quartz;


import com.crazy.portal.config.quartz.annotation.Task;
import com.crazy.portal.entity.ScheduleJob;
import com.crazy.portal.service.ScheduleJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;


@Task(value = "test",scheduleCode = "test")
@DisallowConcurrentExecution
@Slf4j
public class TestJob implements Job{
    @Resource
    private ScheduleJobService scheduleJobService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = scheduleJobService.selectByJobCode("xxx");
            try {
                log.info("==============> test正在执行");
            }catch (Exception e){
            }
    }
}
