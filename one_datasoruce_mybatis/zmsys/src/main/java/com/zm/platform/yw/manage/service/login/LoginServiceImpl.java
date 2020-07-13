package com.zm.platform.yw.manage.service.login;

import java.util.UUID;

import com.zm.platform.common.exception.runtime.ParamIsNullException;
import com.zm.platform.common.util.EmptyUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zm.platform.yw.manage.dao.system.UserDao;
import com.zm.platform.yw.manage.entity.system.SysUser;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private UserDao userDao;

	/**
	 * 新增用户
	 */
	@Override
	public boolean addUser(SysUser user) {
		String loginname = user.getLoginName();
		String password = user.getPassword();
		if(EmptyUtil.isHasEmpty(loginname, password)) {
			throw new ParamIsNullException("用户名或密码为空！");
		}
		//盐
		String salt = UUID.randomUUID().toString();
		user.setSalt(salt);
		//shiro两次md5加密
		password = new SimpleHash("MD5", password, salt, 2).toHex();
		return userDao.saveObject(user);
	}
	
	
}
