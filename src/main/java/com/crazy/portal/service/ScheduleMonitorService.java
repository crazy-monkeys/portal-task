package com.crazy.portal.service;

import com.crazy.portal.dao.ScheduleMonitorMapper;
import com.crazy.portal.entity.ScheduleMonitor;
import com.crazy.portal.util.common.ScheduleEnum;
import com.crazy.portal.util.common.ScheduleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * @author Bill Chan
 * @date 2017年3月14日 下午3:07:41
 */
@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
public class ScheduleMonitorService {

	private static Logger LOGGER = LoggerFactory.getLogger(ScheduleMonitorService.class);

	@Resource
	private ScheduleMonitorMapper scheduleMonitorMapper;
	
	public int insertScheduleMonitor(ScheduleMonitor record){
		return scheduleMonitorMapper.insertSelective(record);
	}

	public int updateScheduleMonitor(ScheduleMonitor record){
		return scheduleMonitorMapper.updateByPrimaryKeySelective(record);
	}

	public ScheduleMonitor getCurrentMonitor(String scheduleCode) {
		return scheduleMonitorMapper.getCurrentMonitor(scheduleCode);
	}
	
	public ScheduleMonitor getLatestDataByCode(String scheduleCode) {
		return scheduleMonitorMapper.getLatestDataByCode(scheduleCode);
	}

	public List<ScheduleMonitor> getErrorScheduleAndNotSendEmail(){
		return scheduleMonitorMapper.getErrorScheduleAndNotSendEmail();
	}

	public void processBefore(String scheduleCode) {
		ScheduleMonitor scheduleMonitor = new ScheduleMonitor();
		scheduleMonitor.setScheduleCode(scheduleCode);
		scheduleMonitor.setScheduleName(ScheduleEnum.getScheduleName(scheduleCode));
		scheduleMonitor.setScheduleStatus(0);
		scheduleMonitor.setBeginTime(new Timestamp(new Date().getTime()));
		try {
			scheduleMonitorMapper.insertSelective(scheduleMonitor);
		} catch (Exception e) {
			LOGGER.error("新增监控调度记录出现异常",e);
		}
	}

	public void processAfter(String scheduleCode) {
		ScheduleMonitor scheduleMonitor = scheduleMonitorMapper.getCurrentMonitor(scheduleCode);
		scheduleMonitor.setEndTime(new Timestamp(new Date().getTime()));
		if(scheduleMonitor.getScheduleStatus().equals(-1)){
			return;
		}
		scheduleMonitor.setScheduleStatus(1);
		ScheduleUtils.scheduleMonitorMap.put(scheduleCode, null);
		try {
			scheduleMonitorMapper.updateByPrimaryKeySelective(scheduleMonitor);
		} catch (Exception e) {
			LOGGER.error("新增监控调度记录出现异常",e);
		}
	}


}
