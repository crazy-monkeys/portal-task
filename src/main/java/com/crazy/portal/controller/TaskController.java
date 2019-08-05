package com.crazy.portal.controller;

import com.crazy.portal.bean.TaskBean;
import com.crazy.portal.config.quartz.LoadPackageClasses;
import com.crazy.portal.config.quartz.annotation.Task;
import com.crazy.portal.entity.ScheduleJob;
import com.crazy.portal.service.ScheduleJobService;
import com.crazy.portal.util.common.Constant;
import com.alibaba.fastjson.JSON;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class TaskController {

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	
	@RequestMapping("/systemStatus")
	public @ResponseBody String systemStatus(){
		return "success";
	}
	
	@Resource
	private ScheduleJobService scheduleJobService;

	@RequestMapping("/ajax/scheduleJob/selectById")
	public @ResponseBody ScheduleJob selectById(int jobId){
		ScheduleJob job = null;
		try {
			job = scheduleJobService.selectById(jobId);
		} catch (Exception e) {
			logger.error("查询任务"+jobId+"失败",e);
		}
		return job;
	}
	
	@RequestMapping("/scheduleJob/list")
	public ModelAndView list(ModelAndView mv,String jobGroup){
		try {
			//分组标签
			mv.addObject("jobGroupList", Constant.getJobGroupList());
			jobGroup = jobGroup == null ? Constant.getJobGroupList().get(0).get("groupCode").toString():jobGroup;
			//获取数据库中已经绑定的值
			List<ScheduleJob> list = scheduleJobService.queryList(jobGroup);
			mv.addObject("scheduleJobVoList", list);
			//运行中的任务
			List<ScheduleJob> executingJobList = scheduleJobService.queryExecutingJobList(jobGroup);
			mv.addObject("executingJobList", executingJobList);
			//扫描自定义注解Task的类
			@SuppressWarnings("unchecked")
			Set<Class<?>> classSet = new LoadPackageClasses(
					Constant.ANNOTATION_COMPONENT_SCAN_PACKAGES, Task.class).getClassSet();
			List<TaskBean> tasks = new ArrayList<TaskBean>();
			for(Class<?> clazz : classSet){
				TaskBean taskBean = new TaskBean();
				Task taskAnnotation = clazz.getAnnotation(Task.class);
				taskBean.setScheduleCode(taskAnnotation.scheduleCode());
				taskBean.setJobDisplayName(taskAnnotation.value());
				taskBean.setClassPath(clazz.getName());
				tasks.add(taskBean);
			}
			Collections.sort(tasks, new Comparator<TaskBean>() {  
		           @Override  
		           public int compare(TaskBean o1, TaskBean o2) { 
		               return o1.getJobDisplayName().compareTo(o2.getJobDisplayName());  
		           }  
		    });
			mv.addObject("readyTasks", tasks);
			mv.addObject("jobGroup", jobGroup);
		} catch (Exception e) {
			logger.error("获取任务列表失败",e);
		}
		mv.setViewName("index");
		return mv;
	}
	
	 /**
     * 暂停
     * @return
     */
    @RequestMapping(value = "/scheduleJob/pause")
    @ResponseBody
    public String pauseScheduleJob(int scheduleJobId) {
        try {
			scheduleJobService.pauseJob(scheduleJobId);
		} catch (Exception e) {
			logger.error("暂定任务失败",e);
			return "error";
		}
        return "success";
    }
    /**
     * 恢复
     * @return
     */
    @RequestMapping(value = "/scheduleJob/resume")
    @ResponseBody
    public String resumeJob(int scheduleJobId) {
    	try {
			scheduleJobService.resumeJob(scheduleJobId);
		} catch (Exception e) {
			logger.error("恢复任务失败",e);
			return "error";
		}
    	return "success";
    }
    /**
     * 添加任务
     *
     * @param scheduleJob
     * @return
     */
    @RequestMapping(value = "/ajax/scheduleJob/add")
    public @ResponseBody String saveScheduleJob(ScheduleJob scheduleJob) {
    	try {
			List<ScheduleJob> list = scheduleJobService.queryList(null);
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getAliasName().equals(scheduleJob.getAliasName())){
					return "double";//已存在
				}
			}
			scheduleJob.setCreateDate(new Timestamp(new Date().getTime()));
			scheduleJobService.insert(scheduleJob);
			return "success";
		}catch (Exception e) {
			if(e.getMessage().equals("errorExpression")){
				logger.info("errorExpression");
				return "errorExpression";
			}
			logger.error("添加任务失败",e);
			return "error";
		}
    }
    /**
     * 修改任务
     *
     * @param scheduleJob
     * @return
     */
    @RequestMapping(value = "/ajax/scheduleJob/update")
    public @ResponseBody String updateScheduleJob(ScheduleJob scheduleJob) {
        try {
        	boolean isValid = CronExpression.isValidExpression(scheduleJob.getCronExpression());
    		if(!isValid){
    			return "errorExpression";
    		}
    		scheduleJob.setUpdateDate(new Timestamp(new Date().getTime()));
			scheduleJobService.delUpdate(scheduleJob);
			return "success";
		} catch (Exception e) {
			logger.error("修改任务失败",e);
			return "error";
		}
    }
    /**
     * 删除任务
     * @return
     */
    @RequestMapping(value = "/scheduleJob/delete")
    @ResponseBody
    public String deleteJob(int scheduleJobId) {
        try {
			scheduleJobService.deleteJob(scheduleJobId);
		} catch (Exception e) {
			logger.error("删除任务失败",e);
			return "error";
		}
        return "success";
    }
    /**
     * 立即运行一次
     * @return
     */
    @RequestMapping(value = "/scheduleJob/runOnce")
    @ResponseBody
    public String runOnce(int scheduleJobId) {
    	try {
			scheduleJobService.runOnce(scheduleJobId);
		} catch (Exception e) {
			logger.error("立即运行一次失败",e);
			return "error";
		}
    	return "success";
    }
	/**
	 * 实时响应运行中的定时任务列表
	 * @param jobGroup
	 * @return
	 */
    @RequestMapping(value = "/scheduleJob/getExecList",produces={"text/event-stream"})
    public @ResponseBody String getExecList(String jobGroup,HttpServletResponse res) {
    	Map<String,Object> map = new HashMap<>();
    	List<ScheduleJob> executingJobList;
    	List<ScheduleJob> list;
		try {
            Thread.sleep(10000);
			list = scheduleJobService.queryList(jobGroup);
			executingJobList = scheduleJobService.queryExecutingJobList(jobGroup);
			map.put("execList", executingJobList);
			map.put("allList", list);
		} catch (Exception e) {
			logger.error("实时响应运行中的定时任务列表",e);
		}
        return "data:"+ JSON.toJSONString(map)+"\n\n";
    }



}
