package com.zm.platform.yw.manage.dao.system;

import java.util.List;

import com.zm.platform.yw.base.BaseDao;
import com.zm.platform.yw.manage.entity.system.UserRole;

public interface UserRoleDao extends BaseDao<UserRole> {

	/**
	 * 批量删除角色
	 * @param roleId
	 */
	int deleteObjectByUserIds(String[] ids);
	
	int deleteObjectByRoleIds(String[] ids);
	
	boolean saveMoreObject(List<UserRole> list);

}
