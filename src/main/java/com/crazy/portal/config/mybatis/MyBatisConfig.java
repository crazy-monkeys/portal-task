/**
 * 
 */
package com.crazy.portal.config.mybatis;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * @author Bill
 * @modifyTime 2017年7月13日 下午2:56:50
 */
@Configuration
public class MyBatisConfig implements TransactionManagementConfigurer{
	
    @Resource(name="dynamicDataSource")
	DataSource dynamicDataSource;
    
    @Resource(name="odsDataSource")
	DataSource odsDataSource;
    
	
    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
    	SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        bean.setTypeAliasesPackage("com.crazy.portal.entity");

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/**/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    @Bean(name = "odsSqlSessionFactory")
    public SqlSessionFactory odsSqlSessionFactory() throws Exception {
    	SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(odsDataSource);
        bean.setTypeAliasesPackage("com.crazy.portal.entity");

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/**/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dynamicDataSource);
	}

}
