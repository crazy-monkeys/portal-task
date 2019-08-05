package com.crazy.portal.dao;

import com.crazy.portal.entity.ScheduleJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScheduleJobMapper {
    int deleteByPrimaryKey(Integer jobId);

    int insert(ScheduleJob record);

    int insertSelective(ScheduleJob record);

    ScheduleJob selectByPrimaryKey(Integer jobId);

    int updateByPrimaryKeySelective(ScheduleJob record);

    int updateByPrimaryKey(ScheduleJob record);
    
    List<ScheduleJob> getAll(@Param("jobGroup")String jobGroup);
    
    ScheduleJob selectByJobName(@Param("jobName")String jobName,@Param("jobGroup")String jobGroup);

    ScheduleJob selectByJobCode(String jobCode);
}