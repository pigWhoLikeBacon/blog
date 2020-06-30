package com.pig.blog.annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class Authentication implements HandlerInterceptor {
	// 视图渲染之后的操作
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	// 处理请求完成后视图渲染之前的处理操作
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	// 进入controller层之前拦截请求
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		Object is = request.getSession().getAttribute("isMaster");
		if (is instanceof Boolean) {
			if (is != null && (Boolean) is) {
				return true;
			} else {
				request.getSession().setAttribute("url", request.getServletPath());
				response.sendRedirect(request.getContextPath()+"/back/loginPage");
		        return false;
			}
		} else {
			request.getSession().setAttribute("url", request.getServletPath());
			response.sendRedirect(request.getContextPath()+"/back/loginPage");
	        return false;
		}
	}

}
