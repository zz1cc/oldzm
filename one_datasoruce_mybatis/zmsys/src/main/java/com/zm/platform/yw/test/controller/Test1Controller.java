package com.zm.platform.yw.test.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zm.platform.yw.manage.entity.system.SysUser;
import com.zm.platform.yw.test.service.TestService;


/**
 * 使用@RestController，无法渲染页面。数据交互时，不需要使用@ResponseBody，并且返回给页面的数据是 json格式
 * 使用@Controller可以渲染页面（跳页面），可以数据交互（需要@ResponseBody配合）
 * @author Administrator
 *
 */
@Controller
public class Test1Controller {
	@Autowired
	private TestService testService;
	/**
	 * 获取所有用户信息
	 * @return
	 */
	//http://localhost:8080/user/listUserBean
	@RequestMapping("/listUserBean")
	public String listUserBean(Model model){
		try {
			String name = "z";
			List<SysUser> userList = testService.listUserBean(name);
			SysUser UserBean = new SysUser();
			model.addAttribute("result", true);
			model.addAttribute("UserBean", UserBean);
			model.addAttribute("users", userList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
	
	@RequestMapping("/getUserBean")
	public String getUserBean(Model model){
		try {
			String id = "1";
			model.addAttribute("user", testService.getUserBean(id));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "user";
	}

	/**
	 * 分页插件
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/testPageHelper")
	public List<Map<String, Object>> testPageHelper(){
		testService.listProvce();
		return null;
	}

	/**
	 * 默认目录为templates
	 * @return
	 */
	@RequestMapping("/")
	public String view(){
	    return "../static/index";
	}
}
