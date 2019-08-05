package com.crazy.portal.util.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class WebEnvironmentParam {

    private static final Logger logger = LoggerFactory.getLogger(WebEnvironmentParam.class);

    private String local;

    public String getLocal() {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            return getSystemUserName() +ip.substring(ip.lastIndexOf(".")+1,ip.length());
        } catch (Exception e) {
            logger.error("",e);
            return ip;
        }
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public static String getSystemUserName(){
        String userName = System.getProperty("user.name");
        return PatternUtil.patternCheck(userName,PatternUtil.CONTAIN_SPECIAL_CHARACTER)
                .replaceAll("").trim();
    }

    public static void main(String[] args) {
        System.out.println(getSystemUserName());
    }
}
