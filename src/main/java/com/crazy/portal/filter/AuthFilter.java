package com.crazy.portal.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 权限过滤器
 * @author xin.xia
 * @date 2017-10-11
 */
@WebFilter(urlPatterns={"/scheduleJob/list","/"})
public class AuthFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse rep = (HttpServletResponse)response;
		Boolean isLogin = req.getSession().getAttribute("isLogin")==null?null:(Boolean)req.getSession().getAttribute("isLogin");
		if(isLogin!= null && isLogin){
			filterChain.doFilter(request, response);
		}else{
			rep.sendRedirect(req.getContextPath()+"/login.html");
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
