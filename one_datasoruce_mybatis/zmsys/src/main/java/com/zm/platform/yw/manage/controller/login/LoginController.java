package com.zm.platform.yw.manage.controller.login;

import com.zm.platform.common.util.EmptyUtil;
import com.zm.platform.common.util.redis.RedisUtil;
import com.zm.platform.common.constant.StatusCode;
import com.zm.platform.yw.result.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zm.platform.yw.manage.service.login.LoginService;
import com.zm.platform.yw.manage.entity.system.SysUser;
import com.zm.platform.yw.manage.service.system.UserService;

@RestController
@RequestMapping(value="/u/",produces="text/plain;charset=utf-8")
public class LoginController {

	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;

	@RequestMapping(value="login",produces="application/json;charset=utf-8")
	public R login(@RequestBody SysUser user){
		if(EmptyUtil.isNotEmpty(user)) {
			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken userToken = new UsernamePasswordToken(user.getLoginName(), user.getPassword());
			try {
	            //进行验证，这里可以捕获异常，然后返回对应信息
				//会触发doGetAuthenticationInfo方法
				subject.login(userToken);
				//验证是否登录成功
				if(subject.isAuthenticated()) {
					//设置过期时间
					user = userService.getUserByLoginName(user.getLoginName());
					subject.getSession().setAttribute("user", user);
					RedisUtil.set("userName", user.getUserName());
					subject.getSession().setTimeout(1800000);
				}
				//subject.checkRole("admin");
				//subject.checkPermissions("query", "add");
				return R.success("登录成功！");
	        } catch (AuthenticationException e) {
				return R.error("用户名或密码错误！");
	        } catch (AuthorizationException e) {
				return R.result(StatusCode.NOPERMISSION, "无权限！", null);
	        }
		}
		return R.error("用户名或密码为空！");
	}
	
	@RequestMapping(value="logout",produces="application/json;charset=utf-8")
	public void logout(){
		SecurityUtils.getSubject().logout();
	}

}

