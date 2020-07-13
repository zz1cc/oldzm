package com.zm.platform.yw.test.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zm.platform.yw.manage.entity.system.SysUser;

@RestController
public class GetDataController {

	/**
	 * var param = {
            "loginname": '111',
            "password": '222',
        }
        $.ajax({
            url: 'http://localhost:8081/zmbk/testGetDataTtype'+t,
            data: JSON.stringify(param),
            type:'post',
            dataType:'json',
            contentType: "application/json",
            success: function (result) {
                console.log(result);
            },
            error:function(reason){
                console.log(reason)
            }
        });
	 * 
	 */
	
	
	/** 暂时接收不到数据 **/
	@RequestMapping(path = "/testGetDataTtype1")
	public String testGetDataTtype1(@RequestParam(value="loginname", required=false) String loginname, 
			@RequestParam(value="password", required=false) String password) {
		System.out.println("type1：");
		System.out.println(loginname + "," + password);
		System.out.println("----------");
		return "1111";
	}

	/** 接收对象 @RequestBody  前台{"aa":11,"bb":22} */
	@RequestMapping("/testGetDataTtype2")
	public String testGetDataTtype2(@RequestBody SysUser param) {
		System.out.println("type2：");
		System.out.println(param);
		System.out.println("----------");
		return "2222";
	}
	
	/** 接受多个参数 **/
	@RequestMapping("/testGetDataTtype3")
	public String testGetDataTtype3(@RequestBody JSONObject jsonObject) {
		System.out.println("type3：");
		System.out.println(jsonObject);
		System.out.println("----------");
		return "3333";
	}

	/** 接收对象 @RequestBody  前台{"aa":11,"bb":22} */
	@RequestMapping("/testGetDataTtype4")
	public String testGetDataTtype4(@RequestBody Map<String, Object> param) {
		System.out.println("type4：");
		System.out.println(param);
		System.out.println("----------");
		return "4444";
	}

	@Autowired
	private HttpServletRequest request;
	 
	@Autowired
	private HttpServletResponse response;
	
	/** 接收请求头 @RequestBody */
	@RequestMapping("/testGetDataTtype5")
	public void testGetDataTtype5(@RequestBody Map<String, Object> param, HttpServletRequest request1) {
	    String p = request.getContextPath();
	    String p1 = request1.getContextPath();
	    Object s = request.getParameter("passowrd");
	    Object s1 = request1.getParameter("passowrd");
	    Map<String, String[]> map = request.getParameterMap();
	    response.addCookie(new Cookie("aaa", "111"));
	    Cookie[] cookies = request.getCookies();
	    if (cookies == null) {
	    	cookies = request1.getCookies();
	    }
	    if (cookies != null) {
		    for (Cookie cookie : cookies) {
		        if ("myCookie".equals(cookie.getName())) {
		    		System.out.println("type5：");
		            System.out.println(cookie.getValue());
		    		System.out.println("----------");
		        }
		        
		    }
	    }
	}
}
