package com.crazy.portal.config.dynamicdb;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * @author Bill
 * @modifyTime 2017年7月13日 下午2:03:55
 */
public class DynamicDataSourceContextHolder {
	
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
	
    public static List<String> dataSourceIds = new ArrayList<String>();

    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static String getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
    
	public static boolean containsDataSource(String dataSourceId){
        return dataSourceIds.contains(dataSourceId);
    }
    
}
