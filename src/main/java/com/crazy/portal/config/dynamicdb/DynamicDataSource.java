package com.crazy.portal.config.dynamicdb;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Desc:
 * @author Bill
 * @modifyTime 2017年7月13日 下午2:05:00
 */
public final class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicDataSourceContextHolder.getDataSourceType();
	}
	
}
