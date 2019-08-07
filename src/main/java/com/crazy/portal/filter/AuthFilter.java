package com.crazy.portal.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 权限过滤器
 * @author xin.xia
 * @date 2017-10-11
 */
public class AuthFilter implements Filter{

	String[] includeUrls = new String[]{"/login", "/static"};

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse rep = (HttpServletResponse)response;
		Boolean isLogin = req.getSession().getAttribute("isLogin")==null?null:(Boolean)req.getSession().getAttribute("isLogin");
		String uri = req.getRequestURI();
		if((isLogin!= null && isLogin) || !isNeedFilter(uri)){
			filterChain.doFilter(request, response);
		}else{
			rep.sendRedirect(req.getContextPath()+"/login");
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

	/**
	 * @Description: 是否需要过滤
	 */
	public boolean isNeedFilter(String uri) {
		for (String includeUrl : includeUrls) {
			if(includeUrl.equals(uri) || uri.contains(includeUrl)) {
				return false;
			}
		}
		return true;
	}
}
