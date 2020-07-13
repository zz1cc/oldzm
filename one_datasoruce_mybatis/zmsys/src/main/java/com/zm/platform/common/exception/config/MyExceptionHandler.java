package com.zm.platform.common.exception.config;

import com.zm.platform.yw.result.R;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zm.platform.common.constant.StatusCode;
import com.zm.platform.common.exception.runtime.CurrentException;
import com.zm.platform.common.exception.runtime.NotLoginException;
import com.zm.platform.common.exception.runtime.ParamIsNullException;

/**
 * 全局异常
 * @author Administrator
 *
 */
@ControllerAdvice
public class MyExceptionHandler {
	private Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);
	
	@ExceptionHandler(value = ParamIsNullException.class)  
    @ResponseBody
    public R bizExceptionHandler(ParamIsNullException e){
		return R.error(e.getMessage(), e);
    }
	
	@ExceptionHandler(value = CurrentException.class)  
    @ResponseBody
    public R bizExceptionHandler(CurrentException e){
		return R.error(e.getMessage(), e);
    }

	/**
	 * 未登录
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = NotLoginException.class)  
    @ResponseBody
    public R bizExceptionHandler(NotLoginException e){
		return R.result(StatusCode.NOLOGIN, "请先登陆！", e);
    }
	
	/**
	 * shiro无访问权限异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = AuthorizationException.class)  
    @ResponseBody
    public R bizExceptionHandler(AuthorizationException e){
		return R.result(StatusCode.NOPERMISSION, "无访问权限！", e);
    }
	
	/**
	 * 处理其他异常
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public R exceptionHandler(Exception e){
		log.error("未知异常！原因是:",e);
		return R.error("系统异常!", e);
	}
	
}
