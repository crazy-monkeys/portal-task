package com.crazy.portal.config.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 * <p>
 * Description:Job对象的实例化过程是在Quartz中进行的,Job中的装配是在spring context中进行的<br />
 * 即依赖的bean并不在spring上下文中所以无法通过@resource装配
 * 原理就是在我们扩展JobFactory创建job的方法，在创建完Job以后进行属性注入。
 * 作用：支持job实例中使用自动装配bean
 * AutowireCapableBeanFactory：主要是为了装配applicationContext管理之外的Bean
 *
 * DefaultBatchConfigurer:
 * spring batch启动时，AbstractBatchConfiguration尝试首先在Spring容器中查找BatchConfigurer，
 * 如果没有找到，则尝试创建它本身，
 * 这时在容器中找到多个DataSource 的实例，因此抛出IllegalStateException异常。
 *
 *
 * </p>
 * @author Bill Chan
 * @date 2017年6月2日 下午5:11:42
 */
@Configuration
@ComponentScan(basePackageClasses = DefaultBatchConfigurer.class)
public class JobFactory extends AdaptableJobFactory {
    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        //调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        //进行注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }


}
