package com.crazy.portal.util.system;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Slf4j
public class ObjectUtil {

    //活动来源
    public static final String[] ACTIVITY_SOURCE = {"HITAO","TOP","TAOBAO","JHS"};

    //终端来源
    public static final String[] TERMINAL_SOURCE = {"WAP","PC"};

	public static boolean isNullOrEmpty(Object obj){
        if (obj == null)  
            return true;  
  
        if (obj instanceof CharSequence)  
            return ((CharSequence) obj).length() == 0;  
  
        if (obj instanceof Collection)  
            return ((Collection) obj).isEmpty();  
  
        if (obj instanceof Map)  
            return ((Map) obj).isEmpty();  
  
        if (obj instanceof Object[]) {  
            Object[] object = (Object[]) obj;  
            if (object.length == 0) {  
                return true;  
            }  
            boolean empty = true;  
            for (int i = 0; i < object.length; i++) {  
                if (!isNullOrEmpty(object[i])) {  
                    empty = false;  
                    break;  
                }  
            }  
            return empty;  
        }  
        return false;  
    }
    public static BigDecimal convertToBigDecimal(Object obj) {
        if (obj == null) {
            return null;
        }
        BigDecimal bd;
        try {
            bd = null;
            if (obj instanceof String) {
                bd = new BigDecimal((String) obj);
            }
            if (obj instanceof Long) {
                bd = new BigDecimal(obj.toString());
            }
            if (obj instanceof BigDecimal) {
                bd = (BigDecimal) obj;
            }
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
        return bd;
    }

    public static Integer convertToInteger(Object obj){
        try {
            if (obj != null) {
                if (obj instanceof String) {
                    return Integer.valueOf((String) obj);
                }
                if (obj instanceof Boolean) {
                    return (Boolean) obj ? 1 : 0;
                }
                if (obj instanceof Long || obj instanceof Integer) {
                    return Integer.parseInt(obj.toString());
                }
            }
        } catch (NumberFormatException e) {
            log.error("",e);
            return null;
        }
        return null;
    }

    public static Long convertToLong(Object obj){
        try {
            if (obj != null) {
                if (obj instanceof String) {
                    return Long.valueOf((String) obj);
                }
                if (obj instanceof Boolean) {
                    return (Boolean) obj ? 1L : 0L;
                }
                if (obj instanceof Long || obj instanceof Integer) {
                    return Long.parseLong(obj.toString());
                }
            }
        } catch (NumberFormatException e) {
            log.error("",e);
            return null;
        }
        return null;
    }
    public static Timestamp convertToTimestamp(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            Timestamp ts = null;
            if (obj instanceof String) {
                String str = (String) obj;
                ts = Timestamp.valueOf(str);
            }
            if (obj instanceof Date) {
                Date date = (Date) obj;
                ts = new Timestamp(date.getTime());
            }
            return ts;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getTerminalSource(String tradeFrom,String[] allSource){
        if(null == tradeFrom || StringUtils.isEmpty(tradeFrom)){
            return null;
        }
        String[] sources = tradeFrom.split(",");
        String result = "";
        for(int j=0;j<allSource.length;j++){
            for(int i = 0;i<sources.length;i++){
                if(allSource[j].equals(sources[i])){
                    result += sources[i] + ",";
                }
            }
        }
        return StringUtils.isEmpty(result)?null:result.substring(0,result.length()-1);
    }
}
