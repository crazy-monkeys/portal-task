/**   
 * <p>
 * Description:<br />
 * </p>
 */
package com.crazy.portal.config.dynamicdb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**   
 * <p>
 * Description:<br />
 * </p>
 * @author Bill Chan
 * @date 2017年7月30日 下午11:01:53
 */
@Component
@Data
@ConfigurationProperties(prefix = "spring.datasource")  
public class DruidConfigProperties {
	private String masterDriverClassName;  
    private String masterUrl;  
    private String masterUsername;  
    private String masterPassword;  
  
    private String odsDriverClassName;
    private String odsUrl;  
    private String odsUsername;  
    private String odsPassword;

	//connection pool
    private Boolean testOnBorrow; 
    private String  type;
    private Integer initialSize;
    private Integer minIdle;  
    private Integer maxActive;  
    private Integer maxWait;  
    private Long timeBetweenEvictionRunsMillis;  
    private Long minEvictableIdleTimeMillis;  
    private String validationQuery;
    private String validationQueryOracle;
    private Boolean testWhileIdle;  
    private Boolean testOnReturn;  
    private Boolean poolPreparedStatements;  
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private String filters;
    private String connectionProperties;
}
