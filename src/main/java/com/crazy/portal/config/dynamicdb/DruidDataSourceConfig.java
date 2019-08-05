package com.crazy.portal.config.dynamicdb;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * <p>
 * Description:<br />
 * </p>
 * @author Bill Chan
 * @date 2017年7月31日 上午12:03:39
 */
@Configuration
public class DruidDataSourceConfig {

	@Resource
	DruidConfigProperties druidConfigProperties;


    @Primary//设置为主要的，当同一个类型存在多个Bean的时候，spring 会默认注入以@Primary注解的bean
    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource dataSource() throws SQLException {  
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(druidConfigProperties.getMasterDriverClassName());  
        druidDataSource.setUrl(druidConfigProperties.getMasterUrl());  
        druidDataSource.setUsername(druidConfigProperties.getMasterUsername());  
        druidDataSource.setPassword(druidConfigProperties.getMasterPassword()); 
        setConnectionPool(druidDataSource);
        if(!druidConfigProperties.getMasterDriverClassName().contains("postgresql")){
            druidDataSource.setValidationQuery(druidConfigProperties.getValidationQueryOracle());
        }
        return druidDataSource;  
    }  
	
    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource odsDataSource() throws SQLException {  
        DruidDataSource druidDataSource = new DruidDataSource();  
        druidDataSource.setDriverClassName(druidConfigProperties.getOdsDriverClassName());  
        druidDataSource.setUrl(druidConfigProperties.getOdsUrl());  
        druidDataSource.setUsername(druidConfigProperties.getOdsUsername());  
        druidDataSource.setPassword(druidConfigProperties.getOdsPassword()); 
        setConnectionPool(druidDataSource);
        if(!druidConfigProperties.getOdsDriverClassName().contains("postgresql")){
            druidDataSource.setValidationQuery(druidConfigProperties.getValidationQueryOracle());
        }
        return druidDataSource;  
    }


	private void setConnectionPool(DruidDataSource druidDataSource)
			throws SQLException {
		druidDataSource.setInitialSize(druidConfigProperties.getInitialSize());
        druidDataSource.setMaxActive(druidConfigProperties.getMaxActive());
        druidDataSource.setMaxWait(druidConfigProperties.getMaxWait());  
        druidDataSource.setTimeBetweenEvictionRunsMillis(druidConfigProperties.getTimeBetweenEvictionRunsMillis());  
        druidDataSource.setMinEvictableIdleTimeMillis(druidConfigProperties.getMinEvictableIdleTimeMillis());  
        druidDataSource.setValidationQuery(druidConfigProperties.getValidationQuery());  
        druidDataSource.setTestWhileIdle(druidConfigProperties.getTestWhileIdle());  
        druidDataSource.setTestOnBorrow(druidConfigProperties.getTestOnBorrow());  
        druidDataSource.setTestOnReturn(druidConfigProperties.getTestOnReturn());  
        druidDataSource.setPoolPreparedStatements(druidConfigProperties.getPoolPreparedStatements());  
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(druidConfigProperties.getMaxPoolPreparedStatementPerConnectionSize());  
        druidDataSource.setFilters(druidConfigProperties.getFilters());  
        druidDataSource.setConnectionProperties(druidConfigProperties.getConnectionProperties());
	}  
	
	@Bean
    public DataSource dynamicDataSource() throws SQLException {  
        DynamicDataSource dynamicDataSource = new DynamicDataSource();  
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();  
        targetDataSources.put("dataSource", dataSource());  
        targetDataSources.put("ods", odsDataSource());
        DynamicDataSourceContextHolder.dataSourceIds.add("ods");
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(dataSource()); 
        return dynamicDataSource;  
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("loginUsername", "bill");
        initParameters.put("loginPassword", "Bill%%123");
        initParameters.put("resetEnable", "false");
//        initParameters.put("allow", "X.X.X.X");
        // (存在共同时，deny优先于allow)
//        initParameters.put("deny", "X.X.X.X");
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions",
                "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean(value = "druid-stat-interceptor")
    public DruidStatInterceptor DruidStatInterceptor() {
        return new DruidStatInterceptor();
    }

    @Bean
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        beanNameAutoProxyCreator.setProxyTargetClass(true);
        // 监控如下两个controller的spring处理情况
        beanNameAutoProxyCreator.setBeanNames("taskController","webServiceController");
        beanNameAutoProxyCreator.setInterceptorNames("druid-stat-interceptor");
        return beanNameAutoProxyCreator;
    }
}
