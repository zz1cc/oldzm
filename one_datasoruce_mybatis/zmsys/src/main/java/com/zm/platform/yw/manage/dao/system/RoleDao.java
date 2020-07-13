package com.zm.platform.yw.manage.dao.system;

import java.util.List;

import com.zm.platform.yw.base.BaseDao;
import com.zm.platform.yw.manage.entity.system.Menu;
import com.zm.platform.yw.manage.entity.system.Role;

public interface RoleDao extends BaseDao<Role> {
	
	List<Menu> getMenuListByRoleId(Long id);
}
