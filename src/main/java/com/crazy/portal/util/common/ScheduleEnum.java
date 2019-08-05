package com.crazy.portal.util.common;

/**
 * 任务调度监控枚举
 * 该枚举具有强依赖性 删除枚举或者修改枚举请谨慎处理
 * @author Bill Chan
 * @date 2017年3月14日 下午3:11:53
 */
public enum ScheduleEnum {
	BATCH_TEST_INFO("Schedule_00","syncScheduleJobDataJob");

	private final String scheduleCode;
	private final String scheduleName;
	
	ScheduleEnum(String scheduleCode,String scheduleName){
		this.scheduleCode = scheduleCode;
		this.scheduleName = scheduleName;
	}
	public static String getScheduleName(String scheduleCode) {  
        for (ScheduleEnum c : ScheduleEnum.values()) {  
            if (c.getScheduleCode().equals(scheduleCode)) {  
                return c.scheduleName;  
            }  
        }  
        return null;  
	 } 
	
	public static String getScheduleCode(String scheduleName) {  
        for (ScheduleEnum c : ScheduleEnum.values()) {  
            if (c.getScheduleName().equals(scheduleName)) {  
                return c.scheduleCode;  
            }  
        }  
        return null;  
	 } 
	public String getScheduleCode() {
		return scheduleCode;
	}
	public String getScheduleName() {
		return scheduleName;
	}
}
