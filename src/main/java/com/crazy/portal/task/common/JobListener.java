/**   
 * <p>
 * Description:<br />
 * </p>
 */
package com.crazy.portal.task.common;

import com.crazy.portal.service.ScheduleMonitorService;
import com.crazy.portal.util.common.ScheduleEnum;
import com.crazy.portal.util.common.ScheduleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**   
 * <p>
 * Description:<br />
 * </p>
 * @author Bill Chan
 * @date 2017年7月17日 下午8:57:04
 */
@Component
public class JobListener extends JobExecutionListenerSupport {

	@Resource
	private ScheduleMonitorService scheduleMonitorService;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		if(jobExecution.getStepExecutions().isEmpty()){
			processBefore(jobExecution);
		}else{
			for(StepExecution step : jobExecution.getStepExecutions()){
				processBefore(step.getJobExecution());
			}
		}
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStepExecutions().isEmpty()){
			processAfter(jobExecution);
		}else{
			for(StepExecution step : jobExecution.getStepExecutions()){
				processAfter(step.getJobExecution());
			}
		}
	}

	/**
	 * <p>
	 * Description:<br />
	 * </p>
	 * @author Bill Chan
	 * @date 2017年4月11日 下午1:55:13
	 * @param jobExecution
	 * @param
	 */
	private void processBefore(JobExecution jobExecution) {
		String scheduleCode = getScheduleCode(jobExecution);
		scheduleMonitorService.processBefore(scheduleCode);
	}

	/**
	 * <p>
	 * Description:<br />
	 * </p>
	 * @author Bill Chan
	 * @date 2017年4月11日 下午1:57:04
	 * @param jobExecution
	 * void
	 */
	private void processAfter(JobExecution jobExecution) {
		String scheduleCode = getScheduleCode(jobExecution);
		scheduleMonitorService.processAfter(scheduleCode);
	}

	/**
	 * <p>
	 * Description:<br />
	 * </p>
	 * @author Bill Chan
	 * @date 2017年4月11日 下午10:33:35
	 * void
	 */
	private void resetScheduleMonitor(String schduleCode) {
		ScheduleUtils.scheduleMonitorMap.put(schduleCode,null);
	}
	
	/**
	 * <p>
	 * Description:<br />
	 * </p>
	 * @author Bill Chan
	 * @date 2017年4月10日 下午3:23:38
	 * @param jobExecution
	 * void
	 */
	private String getScheduleCode(JobExecution jobExecution) {
		String jobName = jobExecution.getJobInstance().getJobName();
		String scheduleCode = ScheduleEnum.getScheduleCode(jobName);
		if(StringUtils.isEmpty(scheduleCode)){
			throw new IllegalArgumentException(String.format(
					"获取调度枚举Code异常>>>> jobName:%s", jobName));
		}
		return scheduleCode;
	}

	
}
