package com.crazy.portal.config.quartz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ExecutorConfig {


    @Resource
    private JobFactory jobFactory;

    /** Set the ThreadPoolExecutor's core pool size. */
    private int corePoolSize = 15;
    /** Set the ThreadPoolExecutor's maximum pool size. */
    private int maxPoolSize = 50;
    /** Set the capacity for the ThreadPoolExecutor's BlockingQueue. */
    private int queueCapacity = 15;


    @Bean
    public Executor myAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("MyExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);
        return factory;
    }

}
