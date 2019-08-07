package com.crazy.portal.controller;

import javax.servlet.http.HttpServletRequest;

import com.crazy.portal.bean.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	/**
	 * 登录验证
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping("/loginCheck")
	public @ResponseBody String loginCheck(String userName, String password, HttpServletRequest request){
		if(userName.equals(this.userName) && password.equals(this.password)){
			request.getSession().setAttribute("isLogin", true);
			return "succeed";
		}else{
			return "fail";
		}
	}
}
