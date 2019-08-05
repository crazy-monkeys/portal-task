package com.crazy.portal.task.postgre.quartz;


import com.crazy.portal.config.quartz.annotation.Task;
import com.crazy.portal.entity.ScheduleJob;
import com.crazy.portal.service.ScheduleJobService;
import com.crazy.portal.service.email.EmailService;
import com.crazy.portal.util.system.ExceptionUtils;
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
    @Resource
    private EmailService emailService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = scheduleJobService.selectByJobCode("xxx");
            try {
            }catch (Exception e){
                emailService.sendSimpleMail("Test Job","警告！警告！警告！出现异常->".concat(ExceptionUtils.getExceptionAllinformation(e)));
            }
    }
}
