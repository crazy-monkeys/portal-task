package com.crazy.portal.util.system;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class RegexUtil {
	 public static boolean checkMobileNumber(String mobileNumber){
			if (StringUtils.isNotBlank(mobileNumber)) {//手机号不为空
			 	Pattern p = Pattern.compile("^[1]{1}[3,4,5,7,8]{1}[0-9]{9}$");
		        Matcher m = p.matcher(mobileNumber.trim());
		        return m.matches();
			}else{
				return false;
			}
	 }
}
