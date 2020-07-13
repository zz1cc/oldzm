package com.zm.platform.yw.manage.service.system;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.zm.platform.yw.manage.entity.system.Role;
import com.zm.platform.yw.manage.entity.system.SysUser;

public interface UserService {
	
	PageInfo<SysUser> getObjectListByPage(Map<String, Object> params);
	
	boolean addObject(SysUser user, String checkedKeys);
	
	boolean updateObject(SysUser user, String checkedKeys);
	
	SysUser getUserByLoginName(String loginname);
	
	List<Role> getRoleList(Long userId);
	
	boolean deleteObjectByIds(String ids);
	
	boolean modifyPassword(SysUser user);
	
} 
