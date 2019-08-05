/**
 * 
 */
package com.crazy.portal.service;

import com.crazy.portal.dao.ScheduleJobMapper;
import com.crazy.portal.entity.ScheduleJob;
import com.crazy.portal.util.common.CommonEnum;
import com.crazy.portal.util.common.ScheduleUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Bill
 * @created 2017年7月16日 下午3:57:42
 */
@Service
public class ScheduleJobService {
    public static Logger logger = LoggerFactory.getLogger(ScheduleJobService.class);
		
    /** 调度工厂Bean */
    @Resource
    private Scheduler scheduler;

    @Resource
	private ScheduleJobMapper scheduleJobMapper;

    public void initScheduleJob() throws Exception{
    	List<ScheduleJob> jobList = scheduleJobMapper.getAll("data-treating");
        if (jobList.isEmpty()) {
            return;
        }
        for (ScheduleJob scheduleJob : jobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler,
                scheduleJob.getJobName(), scheduleJob.getJobGroup());

            //不存在，创建一个
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            }
        }
    }

	public List<ScheduleJob> queryList(String jobGroup) throws Exception{
	    List<ScheduleJob> list = scheduleJobMapper.getAll(jobGroup);
        for (ScheduleJob vo : list) {

            JobKey jobKey = ScheduleUtils.getJobKey(vo.getJobName(), vo.getJobGroup());
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            if (triggers.isEmpty()) {
                continue;
            }

            //这里一个任务可以有多个触发器， 但是我们一个任务对应一个触发器，所以只取第一个即可，清晰明了
            Trigger trigger = triggers.iterator().next();
            vo.setJobTrigger(trigger.getKey().toString());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            vo.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                vo.setCronExpression(cronExpression);
            }
        }
	    return list;
	}
	/**
     * 获取运行中的任务列表
     * @return
     */
	public List<ScheduleJob> queryExecutingJobList(String jobGroup) throws Exception{
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
			for (JobKey jobKey : jobKeys) {
			    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			    for (Trigger trigger : triggers) {
			        ScheduleJob job = new ScheduleJob();
			        job.setJobName(jobKey.getName());
			        job.setJobGroup(jobKey.getGroup());
			        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			        job.setJobStatus(triggerState.name());
			        if (trigger instanceof CronTrigger) {
			            CronTrigger cronTrigger = (CronTrigger) trigger;
			            String cronExpression = cronTrigger.getCronExpression();
			            job.setCronExpression(cronExpression);
			        }
			        ScheduleJob t = scheduleJobMapper.selectByJobName(jobKey.getName(),jobGroup);
			        if(t != null){
				        job.setAliasName(t.getAliasName());
				        job.setCreateDate(t.getCreateDate());
				        job.setIsSync(t.getIsSync());
				        job.setJobId(t.getJobId());
				        job.setUpdateDate(t.getUpdateDate());
				        job.setJobDesc(t.getJobDesc());
				        job.setJobTrigger(trigger.getKey().toString());
				        jobList.add(job);
			        }
			    }
			}
			return jobList;
	}
	/**
     * 新增
     * @param
     * @return
     */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int insert(ScheduleJob scheduleJob) throws Exception{
		scheduleJob.setJobStatus(CommonEnum.JobStatusType.PAUSED.getCode());
		scheduleJob.setIsSync(true);
		boolean isValid = CronExpression.isValidExpression(scheduleJob.getCronExpression());
		if(!isValid){
			throw new IllegalArgumentException("errorExpression");
		}
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        return scheduleJobMapper.insertSelective(scheduleJob);
    }
	/**
     *  暂停任务
     * @param scheduleJobId
     */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void pauseJob(int scheduleJobId) throws Exception{
		  ScheduleJob scheduleJob = scheduleJobMapper.selectByPrimaryKey(scheduleJobId);
	      scheduleJob.setJobStatus(CommonEnum.JobStatusType.PAUSED.getCode());
	      int result = scheduleJobMapper.updateByPrimaryKeySelective(scheduleJob);
	      if(result > 0){
	    	  ScheduleUtils.pauseJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
	      }
	}
	/**
	 *  恢复任务
	 * @param scheduleJobId
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void resumeJob(int scheduleJobId) throws Exception{
		ScheduleJob scheduleJob = scheduleJobMapper.selectByPrimaryKey(scheduleJobId);
		scheduleJob.setJobStatus(CommonEnum.JobStatusType.NORMAL.getCode());
	      int result = scheduleJobMapper.updateByPrimaryKeySelective(scheduleJob);
	      if(result > 0){
	    	  ScheduleUtils.resumeJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
	      }
	}
	/**
	 *  删除任务
	 * @param scheduleJobId
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void deleteJob(int scheduleJobId) throws Exception{
		ScheduleJob scheduleJob = scheduleJobMapper.selectByPrimaryKey(scheduleJobId);
		ScheduleUtils.deleteJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduleJobMapper.deleteByPrimaryKey(scheduleJobId);
	}
	/**
	 *  立即运行一次
	 * @param scheduleJobId
	 */
	public void runOnce(int scheduleJobId) throws Exception{
		ScheduleJob scheduleJob = scheduleJobMapper.selectByPrimaryKey(scheduleJobId);
		ScheduleUtils.runOnce(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
	}
	 /**
     * 删除重新创建
     * @param scheduleJob
     */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void delUpdate(ScheduleJob scheduleJob) throws Exception{
		//数据库直接更新即可
        scheduleJob.setUpdateDate(new Timestamp(new Date().getTime()));
        scheduleJobMapper.updateByPrimaryKeySelective(scheduleJob);
    	ScheduleJob sche = scheduleJobMapper.selectByPrimaryKey(scheduleJob.getJobId());
    	//先删除
        ScheduleUtils.deleteJob(scheduler, sche.getJobName(),sche.getJobGroup());
        //再创建
        sche.setIsSync(true);
        ScheduleUtils.createScheduleJob(scheduler, sche);
	}

	/**
	 * 主键查询
	 */
	public ScheduleJob selectById(int jobId) throws Exception{
		return scheduleJobMapper.selectByPrimaryKey(jobId);
	}

	public ScheduleJob selectByJobCode(String jobCode){
		return scheduleJobMapper.selectByJobCode(jobCode);
	}
}
