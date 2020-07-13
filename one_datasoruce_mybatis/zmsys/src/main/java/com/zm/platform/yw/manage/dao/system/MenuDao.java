package com.zm.platform.yw.manage.dao.system;

import java.util.List;

import com.zm.platform.yw.base.BaseDao;
import com.zm.platform.yw.manage.entity.system.Menu;

public interface MenuDao extends BaseDao<Menu> {
	
	List<Menu> getObjectByRoleIds(String[] ids);

}
