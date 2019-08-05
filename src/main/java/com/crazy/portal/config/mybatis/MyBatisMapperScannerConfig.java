/**
 * 
 */
package com.crazy.portal.config.mybatis;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Desc:
 * @author Bill
 * @modifyTime 2017年7月13日 下午2:59:36
 */
@Configuration
//注意，由于MapperScannerConfigurer执行的比较早，所以必须有下面的注解
@AutoConfigureAfter(MyBatisConfig.class)
public class MyBatisMapperScannerConfig {
	
	@Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.crazy.portal.dao");
        return mapperScannerConfigurer;
    }

    @Bean
    public  TransactionTemplate transactionTemplate(PlatformTransactionManager annotationDrivenTransactionManager){
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(annotationDrivenTransactionManager);
        return transactionTemplate;
    }
}
