package com.crazy.portal.util.common;

/**
 * Created by Bill on 2017/12/21.
 */
public class CommonEnum {

    public enum JobStatusType {
        NORMAL("执行中", "NORMAL"), PAUSED("暂停", "PAUSED");
        private final String msg;
        private final String code;

        JobStatusType(String msg, String code) {
            this.code = code;
            this.msg = msg;
        }
        public String getCode() {
            return code;
        }
    }


    public enum RESPONSE_INFO{
        SUCCESS(1,"操作成功"),FAILURE(-1,"操作失败");

        private final int code;
        private final String msg;

        RESPONSE_INFO(int code,String msg){
            this.code = code;
            this.msg = msg;
        }
        public int getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
    }
}
