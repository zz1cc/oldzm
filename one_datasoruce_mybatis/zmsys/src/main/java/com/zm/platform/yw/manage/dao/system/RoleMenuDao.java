package com.zm.platform.yw.manage.dao.system;

import java.util.List;

import com.zm.platform.yw.base.BaseDao;
import com.zm.platform.yw.manage.entity.system.RoleMenu;

public interface RoleMenuDao extends BaseDao<RoleMenu> {
	
	boolean saveMoreObject(List<RoleMenu> list);
	
	boolean updateMoreObject(String[] ids);
	
	int deleteObjectByRoleIds(String[] ids);
	
	int deleteObjectByMenuIds(String[] ids);
}
