package com.crazy.portal.entity;

import java.util.Date;

public class ScheduleMonitor {
    private Long scheduleMonitorId;

    private String scheduleName;

    private String scheduleCode;

    private String scheduleDetail;

    private Date beginTime;

    private Date endTime;

    private Integer scheduleStatus;

    private Date lastRecordTime;

    private Integer hasSendEmail;

    public Integer getHasSendEmail() {
        return hasSendEmail;
    }

    public void setHasSendEmail(Integer hasSendEmail) {
        this.hasSendEmail = hasSendEmail;
    }

    public Long getScheduleMonitorId() {
        return scheduleMonitorId;
    }

    public void setScheduleMonitorId(Long scheduleMonitorId) {
        this.scheduleMonitorId = scheduleMonitorId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName == null ? null : scheduleName.trim();
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode == null ? null : scheduleCode.trim();
    }

    public String getScheduleDetail() {
        return scheduleDetail;
    }

    public void setScheduleDetail(String scheduleDetail) {
        this.scheduleDetail = scheduleDetail == null ? null : scheduleDetail.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    public Integer getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(Integer scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public Date getLastRecordTime() {
        return lastRecordTime;
    }

    public void setLastRecordTime(Date lastRecordTime) {
        this.lastRecordTime = lastRecordTime;
    }
}