/**   
 * <p>
 * Description:<br />
 * </p>
 */
package com.crazy.portal.task.common;

import com.crazy.portal.entity.ScheduleMonitor;
import com.crazy.portal.service.ScheduleMonitorService;
import com.crazy.portal.service.email.EmailService;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * Description:<br />
 * </p>
 * 
 * @author Bill Chan
 * @date 2017年7月17日 下午4:13:50
 */
@EnableScheduling
@Component
@DisallowConcurrentExecution
public class SystemJob {

	@Resource
	private ScheduleMonitorService scheduleMonitorService;

	@Resource
	private EmailService emailService;

	@Scheduled(cron = "0 0/1 * * * ? ")
	public void moitor(){
		List<ScheduleMonitor> errorScheduleMonotors = scheduleMonitorService.getErrorScheduleAndNotSendEmail();
		for(ScheduleMonitor scheduleMonitor:errorScheduleMonotors){
			String content = String.format("监控id:%s,监控job:%s,错误日志:\n\n%s",
					scheduleMonitor.getScheduleMonitorId(),scheduleMonitor.getScheduleName(),scheduleMonitor.getScheduleDetail());
			emailService.sendSimpleMail("数据同步异常通知",content);
			scheduleMonitor.setHasSendEmail(1);
			scheduleMonitorService.updateScheduleMonitor(scheduleMonitor);
		}
	}
}
