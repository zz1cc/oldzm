package com.zm.platform.yw.test.service;

import java.util.List;
import java.util.Map;

import com.zm.platform.yw.manage.entity.system.SysUser;


public interface TestService {
	/**
	 * 获取用户信息的列表
	 * @return
	 */
	List<SysUser> listUserBean(String name);
	
	SysUser getUserBean(String id);

	List<Map<String, Object>> listProvce();
}
