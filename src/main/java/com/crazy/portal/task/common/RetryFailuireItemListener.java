/**   
 * <p>
 * Description:<br />
 * </p>
 */
package com.crazy.portal.task.common;

import com.crazy.portal.util.system.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

/**   
 * <p>
 * Description:<br />
 * </p>
 * @author Bill Chan
 * @date 2017年7月17日 下午10:13:18
 */

public class RetryFailuireItemListener implements RetryListener {
	
	private static final Logger logger = LoggerFactory.getLogger(RetryFailuireItemListener.class);  
	
	@Override
	public <T, E extends Throwable> void close(RetryContext arg0,
			RetryCallback<T, E> arg1, Throwable arg2) {
		
	}

	@Override
	public <T, E extends Throwable> void onError(RetryContext context, 
			RetryCallback<T, E> callback,Throwable throwable) {
		
		logger.error(String.format("【重试异常】：%s", ExceptionUtils.getExceptionAllinformation(throwable)));
	}

	@Override
	public <T, E extends Throwable> boolean open(RetryContext arg0,
			RetryCallback<T, E> arg1) {
		return true;  
	}

}
