package com.crazy.portal.task.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class BatchJobExcetor {

	private final static Logger logger = LoggerFactory.getLogger(BatchJobExcetor.class);

	@Resource
	private JobLauncher jobLauncher;

	public final String TASK_STARTING_KEY = "==========================任务【{}】-->正在执行!";
	public final String TASK_FAILED_KEY = "==========================任务【{}】-->执行失败!";
	public final String TASK_EXCEPTION_KEY = "==========================任务【{}】-->出现异常:";
	public final String TASK_DOINE_KEY = "==========================任务【{}】-->完成处理!";

	/**
	 * @Author: Bill
	 * @param parames 需要传入到batch.job中的参数 <String k Object v>
	 * @param job
	 * @Desc:
	 * @Date: 2017/10/25 11:10
	 */
	public void excuteBatchJob(Map<String,?> parames, org.springframework.batch.core.Job job){

		logger.info(TASK_STARTING_KEY,job.getName());
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
				.addDate(String.valueOf(new Date().getTime()), new Date());
		for(Map.Entry<String, ?> entry : parames.entrySet()){
			String key = entry.getKey();
			Object value = entry.getValue();
			if(value instanceof String){
				jobParametersBuilder.addString(key,(String)value);
			}else if(value instanceof Double){
				jobParametersBuilder.addDouble(key,(Double)value);
			}else if(value instanceof Long || value instanceof Integer || value instanceof Short){
				jobParametersBuilder.addLong(key,Long.parseLong(value.toString()));
			}else if(value instanceof Date){
				jobParametersBuilder.addDate(key,(Date)value);
			}
		}
		try{
			JobParameters param = jobParametersBuilder.toJobParameters();
			JobExecution jobExecution = jobLauncher.run(job,param);
			while (!jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
				if(jobExecution.getStatus().equals(BatchStatus.FAILED)){
					logger.error(TASK_FAILED_KEY,job.getName());
					throw new RuntimeException();
				}
				Thread.sleep(3000);
			}
		}catch(Exception e){
			logger.error(TASK_EXCEPTION_KEY,e);
			throw new RuntimeException();
		}
		logger.info(TASK_DOINE_KEY,job.getName());
	}

	/**
	 * @Author: Bill
	 * @param key
	 * @param value
	 * @Desc: 一般只有一个参数,重载一下
	 * @Date: 2017/10/25 11:26
	 */
	public void excuteBatchJob(String key,Object value,org.springframework.batch.core.Job job){
		Assert.notNull(key);
		Assert.notNull(value);
		Map<String,Object> mapParam = new HashMap<String, Object>();
		mapParam.put(key,value);
		this.excuteBatchJob(mapParam,job);
	}

	/**
	 * @Author: Bill
	 * @Desc: 当然可能也没有参数了，再重载一下
	 * @Date: 2017/10/25 11:47
	 */
	public void excuteBatchJob(org.springframework.batch.core.Job job){
		this.excuteBatchJob(new HashMap<String, Object>(),job);
	}
}
