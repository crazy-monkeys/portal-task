package com.crazy.portal.controller;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录
 * @author xin.xia
 * @date 2017-10-11
 */
@Slf4j
@Controller
public class LoginController {
	
	@Value("${login.username}")
	String userName;
	@Value("${login.password}")
	String password;
	
	@RequestMapping("/login.html")
	public String login(){
		return "login";
	}
	/**
	 * 登录验证
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping("/loginCheck.html")
	@ResponseBody
	public String loginCheck(String userName,String password,HttpServletRequest req){
		if(userName.equals(this.userName) && password.equals(this.password)){
			req.getSession().setAttribute("isLogin", true);
			return "succeed";
		}else{
			return "fail";
		}
	}
}
