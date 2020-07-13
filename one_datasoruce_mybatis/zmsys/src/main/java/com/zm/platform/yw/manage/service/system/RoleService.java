package com.zm.platform.yw.manage.service.system;

import java.util.Map;
import com.github.pagehelper.PageInfo;
import com.zm.platform.yw.manage.entity.system.Role;

public interface RoleService {

	PageInfo<Role> getObjectListByPage(Map<String, Object> params);
	
	Role getObjectById(Long id);
	
	boolean addOrUpdateObject(Role menu, String checkedKeys, String halfCheckKeys);
	
	boolean deleteObjectByIds(String ids);
}
