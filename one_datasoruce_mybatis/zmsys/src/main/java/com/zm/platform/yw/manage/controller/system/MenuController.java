package com.zm.platform.yw.manage.controller.system;

import java.util.List;

import com.zm.platform.yw.result.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.zm.platform.yw.manage.entity.system.Menu;
import com.zm.platform.yw.manage.service.system.MenuService;

@RestController
@RequestMapping(value="/menu/",produces="text/plain;charset=utf-8")
public class MenuController {

	@Autowired
	private MenuService menuService;
	
	@RequiresPermissions("sys:menu:add")
	@RequestMapping(value="saveObject",produces="application/json;charset=utf-8")
	public R saveObject(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			Menu menu = jsonObject.getObject("menu", Menu.class);
			if(menu != null){
				if(menuService.saveObject(menu)) {
					return R.success("新增成功！", menu);
				}
				return R.error("新增失败！", menu);
			}
		}
		return R.error("传递参数为空！");
	}

	@RequiresPermissions("sys:menu:update")
	@RequestMapping(value="updateObject",produces="application/json;charset=utf-8")
	public R updateObject(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			Menu menu = jsonObject.getObject("menu", Menu.class);
			if(menu != null){
				if(menuService.updateObject(menu)) {
					return R.success("修改成功！", menu);
				}
				return R.error("修改失败！", menu);
			}
		}
		return R.error("传递参数为空！");
	}

	@RequiresPermissions("sys:menu:delete")
	@RequestMapping(value="deleteObjectByIds",produces="application/json;charset=utf-8")
	public R deleteObjectByIds(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			String ids = jsonObject.getString("ids");
			if(ids != null){
				if(menuService.deleteObjectByIds(ids)) {
					return R.success("修改成功！");
				}
				return R.error("修改失败！", ids);
			}
		}
		return R.error("传递参数为空！");
	}

	@RequiresPermissions("/system/menu")
	@RequestMapping(value="getObjectListByPage",produces="application/json;charset=utf-8")
	public R getObjectListByPage(){
		List<Menu> menuList = menuService.getAllMenuList();
		if(menuList != null && menuList.size() > 0){
			return R.success(menuList);
		}
		return R.error("未查询到数据");
	}

	@RequestMapping(value="getAllMenuList",produces="application/json;charset=utf-8")
	public R getAllMenuList(){
		List<Menu> menuList = menuService.getAllMenuList();
		if(menuList != null && menuList.size() > 0){
			return R.success(menuList);
		}
		return R.error("未查询到数据");
	}

	@RequestMapping(value="getObjectByRoleIds",produces="application/json;charset=utf-8")
	public R getObjectByRoleIds(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			String ids = jsonObject.getString("ids");
			if (ids != null) {
				List<Menu> menuList = menuService.getObjectByRoleIds(ids);
				if(menuList != null && menuList.size() > 0){
					return R.success(menuList);
				}
				return R.error("未查询到数据");
			}
		}
		return R.error("传递参数为空！");
	}
	
	@RequestMapping(value="getAsideMenuList",produces="application/json;charset=utf-8")
	public R getAsideMenuList(){
		List<Menu> menuList = menuService.getAsideMenuList();	
		if(menuList != null && menuList.size() > 0){
			return R.success(menuList);
		}
		return R.error();
	}
}
