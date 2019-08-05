package com.crazy.portal.event;

import com.crazy.portal.service.ScheduleJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


/**
 * 定时任务初始化
 * @author zq
 *
 */
@Component
public class SystemInitEvent {

    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemInitEvent.class);

    /** 定时任务service */
    @Resource
    private ScheduleJobService scheduleJobService;

    /**
     * 项目启动时初始化
     */
    @PostConstruct
    public void init() throws Exception{
        initialScheduleJob();
    }

    private void initialScheduleJob() {
        if (LOGGER.isInfoEnabled()) {
        	LOGGER.info(">  >  >  >  >  schedule jobs init [begin]      >  >  >  >  >  ");
        }
        try {
			scheduleJobService.initScheduleJob();
		} catch (Exception e) {
			LOGGER.error("定时任务初始化失败",e);
		}

        if (LOGGER.isInfoEnabled()) {
        	LOGGER.info(">  >  >  >  >  schedule jobs init [end]      >  >  >  >  >  ");
        }
    }

}
