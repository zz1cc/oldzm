package com.zm.platform.common.interceptor;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zm.platform.common.exception.runtime.NotLoginException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor {

	@Value("${server.port}")//取配置文件的值
	private  String contextPath;

	/**
     * 进入controller层之前拦截请求
     * 返回值：表示是否将当前的请求拦截下来  false：拦截请求，请求别终止。true：请求不被拦截，继续执行
     * Object obj:表示被拦的请求的目标对象（controller中方法）
     */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//请求路径
		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		//无需验证的请求
		List<String> list = new ArrayList<String>();
		list.add("/u/login");
		list.add("/u/logout");
		for(String url: list){
			if(path.equals(contextPath + url)){
				return true;
			}
			if(path.startsWith(contextPath + "/test/")){
				return true;
			}
		}
		//验证是否登陆
		try {
			Subject subject = SecurityUtils.getSubject();
			//验证是否登陆
			if(!subject.isAuthenticated()) {
				throw new NotLoginException("请重新登陆");
			}
		} catch (Exception e) {
			throw new NotLoginException("请重新登陆");
		}
		return true;
	}

	/**
	 * 处理请求完成后视图渲染之前的处理操作
     * 通过ModelAndView参数改变显示的视图，或发往视图的方法
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 视图渲染之后的操作
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
