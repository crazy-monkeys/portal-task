package com.crazy.portal.task.common;

import com.crazy.portal.entity.ScheduleMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class BaseWriter {

	private static Logger LOGGER = LoggerFactory.getLogger(BaseWriter.class);

	protected int count;
	protected ScheduleMonitor scheduleMonitor;
	protected Date lastRecordTime = null;

	protected int getRowCount(int size){
		count += size;
		return count;
	}

	protected void setLastRecordTime(Date date){
		if(date == null){
			LOGGER.info("'INSERT_DATE' is null");
		}else{
			Date currentRecordTime = date;
			if(lastRecordTime == null){
				this.lastRecordTime = currentRecordTime;
			}else{
				this.lastRecordTime = currentRecordTime.after(lastRecordTime)?currentRecordTime:lastRecordTime;
			}
		}
	}
}
