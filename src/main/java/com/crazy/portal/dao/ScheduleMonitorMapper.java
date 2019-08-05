package com.crazy.portal.dao;

import org.apache.ibatis.annotations.Param;

import com.crazy.portal.entity.ScheduleMonitor;

import java.util.List;

public interface ScheduleMonitorMapper {
    int insertSelective(ScheduleMonitor record);

    int updateByPrimaryKeySelective(ScheduleMonitor record);
    
    ScheduleMonitor getCurrentMonitor(@Param("scheduleCode") String scheduleCode);
    
    ScheduleMonitor getLatestDataByCode(@Param("scheduleCode") String scheduleCode);

    List<ScheduleMonitor> getErrorScheduleAndNotSendEmail();
}