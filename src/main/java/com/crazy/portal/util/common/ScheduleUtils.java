/**
 * 
 */
package com.crazy.portal.util.common;

import java.util.HashMap;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crazy.portal.entity.ScheduleJob;
import com.crazy.portal.entity.ScheduleMonitor;

/**
 * @author Bill
 * @created 2017年7月16日 下午4:16:14
 */
public class ScheduleUtils {
	
	public static Map<String, ScheduleMonitor> 
	scheduleMonitorMap = new HashMap<String, ScheduleMonitor>();

	/** 日志对象 */
	private static final Logger logger = LoggerFactory.getLogger(ScheduleUtils.class);

	public static final String START_TIME = "startTime";
	public static final String END_TIME = "endTime";

	/**
	 * 获取触发器key
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public static TriggerKey getTriggerKey(String jobName, String jobGroup) {

		return TriggerKey.triggerKey(jobName, jobGroup);
	}

	/**
	 * 获取表达式触发器
	 * 
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public static CronTrigger getCronTrigger(Scheduler scheduler, String jobName, String jobGroup) {

		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			return (CronTrigger) scheduler.getTrigger(triggerKey);
		} catch (SchedulerException e) {
			logger.error("获取定时任务CronTrigger出现异常", e);
			return null;
		}
	}

	/**
	 * 创建任务
	 *
	 * @param scheduler
	 *            the scheduler
	 * @param scheduleJob
	 *            the schedule job
	 */
	public static void createScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob) {
		createScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup(),
				scheduleJob.getCronExpression(), scheduleJob);
	}

	/**
	 * 创建定时任务
	 * 
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 * @param cronExpression
	 * @param scheduleJob
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void createScheduleJob(Scheduler scheduler, String jobName, String jobGroup, String cronExpression,ScheduleJob scheduleJob) {
		try {

			Class jobClass = Class.forName(scheduleJob.getAliasName());

			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();

			// 放入参数，运行时的方法可以获取
			jobDetail.getJobDataMap().put(scheduleJob.getAliasName(), scheduleJob);

			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
					.withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);

			if (scheduleJob.getJobStatus().equals(CommonEnum.JobStatusType.PAUSED.toString())) {
				scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
			}
		} catch (Exception e) {
			logger.error("创建定时任务失败", e);
		}
	}

	/**
	 * 获取jobKey
	 *
	 * @param jobName
	 *            the job name
	 * @param jobGroup
	 *            the job group
	 * @return the job key
	 */
	public static JobKey getJobKey(String jobName, String jobGroup) {

		return JobKey.jobKey(jobName, jobGroup);
	}

	/**
	 * 暂停任务
	 * 
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void pauseJob(Scheduler scheduler, String jobName, String jobGroup) {

		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			logger.error("暂停定时任务失败", e);
		}
	}

	/**
	 * 恢复任务
	 * 
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void resumeJob(Scheduler scheduler, String jobName, String jobGroup) {

		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			logger.error("恢复定时任务失败", e);
		}
	}

	/**
	 * 运行一次任务
	 * 
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void runOnce(Scheduler scheduler, String jobName, String jobGroup) {
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			logger.error("运行一次定时任务失败", e);
		}
	}

	/**
	 * 删除定时任务
	 *
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void deleteJob(Scheduler scheduler, String jobName, String jobGroup) {
		try {
			scheduler.deleteJob(getJobKey(jobName, jobGroup));
		} catch (SchedulerException e) {
			logger.error("删除定时任务失败", e);
		}
	}
}
