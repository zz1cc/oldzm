package com.zm.platform.yw.manage.service.system;

import java.util.List;

import com.zm.platform.yw.manage.entity.system.Menu;


public interface MenuService {

	List<Menu> getAllMenuList();

	List<Menu> getAsideMenuList();

	List<Menu> getObjectByRoleIds(String ids);
	
	boolean saveObject(Menu menu);
	
	boolean updateObject(Menu menu);
	
	boolean deleteObjectByIds(String ids);
	
}
