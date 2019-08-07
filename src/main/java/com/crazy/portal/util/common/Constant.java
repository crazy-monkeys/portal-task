package com.crazy.portal.util.common;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bill on 2017/12/21.
 */
public class Constant {

    /** RAW_DATA**/
    /** spring batch **/
    public static final String RECORDTIME = "recordTime";

    public static final String[] ANNOTATION_COMPONENT_SCAN_PACKAGES = {"com.crazy.portal"};


    /** RBK 订单状态**/
    public static final String TRADE_FINISHED = "TRADE_FINISHED";//交易成功
    public static final String TRADE_CLOSED = "TRADE_CLOSED";//付款以后用户退款成功，交易自动关闭

    /**系统变量key**/
    public static final String SYSTEM_USERNAME = "user.name";

    public static String getTerminalSource(String tradeFrom,String[] allSource){
        if(StringUtils.isEmpty(tradeFrom)){
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

    public static List<Map<String,Object>> getJobGroupList(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("groupCode", "portal-task");
        map.put("groupName", "portal-task-group");
        list.add(map);
        return list;
    }
}
