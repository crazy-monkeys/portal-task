package com.crazy.portal.entity;

import java.sql.Timestamp;

public class ScheduleJob {
    
    private Integer jobId;

    private String jobName;

    private String jobGroup;

    private String jobStatus;

    private String cronExpression;

    private String jobDesc;

    private String aliasName;

    private String jobTrigger;

    private Boolean isSync;

    private Timestamp createDate;

    private Timestamp updateDate;
    
    private String jobConfigParams;
    
    private String jobCode;
    
	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getJobConfigParams() {
		return jobConfigParams;
	}

	public void setJobConfigParams(String jobConfigParams) {
		this.jobConfigParams = jobConfigParams;
	}
	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup == null ? null : jobGroup.trim();
    }

    public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc == null ? null : jobDesc.trim();
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName == null ? null : aliasName.trim();
    }

    public String getJobTrigger() {
        return jobTrigger;
    }

    public void setJobTrigger(String jobTrigger) {
        this.jobTrigger = jobTrigger == null ? null : jobTrigger.trim();
    }



	public Boolean getIsSync() {
		return isSync;
	}

	public void setIsSync(Boolean isSync) {
		this.isSync = isSync;
	}

	public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }
}