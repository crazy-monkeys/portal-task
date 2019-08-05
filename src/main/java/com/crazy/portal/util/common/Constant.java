package com.crazy.portal.util.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bill on 2017/12/21.
 */
public class Constant {

    public static List<Map<String,Object>> getJobGroupList(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("groupCode", "data-treating");
        map.put("groupName", "Data-treating-group");
        list.add(map);
        return list;
    }
}
