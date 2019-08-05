package com.crazy.portal.util.common;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {

    /**正则**/
    public static final String CONTAIN_CHIANESE = "[\\u4e00-\\u9fa5]";
    public static final String CONTAIN_SPECIAL_CHARACTER="[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";


    public static Matcher patternCheck(String str,String patternExpression){
        Pattern pattern = Pattern.compile(patternExpression);
        return pattern.matcher(str);
    }

    public static boolean containChianese(String str){
        return StringUtils.isEmpty(str)?false:patternCheck(str,CONTAIN_CHIANESE).find();
    }

    public static boolean containSpecialCharacter(String str){
        return StringUtils.isEmpty(str)?false:patternCheck(str,CONTAIN_SPECIAL_CHARACTER).find();
    }
}
