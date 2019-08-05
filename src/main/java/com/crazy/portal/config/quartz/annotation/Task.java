/**
 * 
 */
package com.crazy.portal.config.quartz.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bill
 * @created 2017年7月16日 下午4:38:43
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Task {
	String value() default "";
	String scheduleCode() default "";
}
