package com.zm.platform.yw.manage.controller.system;

import java.util.HashMap;
import java.util.Map;

import com.zm.platform.common.util.EmptyUtil;
import com.zm.platform.yw.result.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zm.platform.yw.manage.entity.system.Role;
import com.zm.platform.yw.manage.service.system.RoleService;

@RestController
@RequestMapping(value="/role/",produces="text/plain;charset=utf-8")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value="addOrUpdateObject",produces="application/json;charset=utf-8")
	public R addOrUpdateObject(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			Role role = jsonObject.getObject("role", Role.class);
			String checkedKeys = jsonObject.getString("checkedKeys");
			String halfCheckKeys = jsonObject.getString("halfCheckKeys");
			if(role != null){
				if(EmptyUtil.isNotEmpty(role.getId())){
					SecurityUtils.getSubject().checkPermissions("sys:role:update");
				}else{
					SecurityUtils.getSubject().checkPermissions("sys:role:add");
				}
				if(roleService.addOrUpdateObject(role, checkedKeys, halfCheckKeys)) {
					return R.success("修改成功！", role);
				}
				return R.error("修改失败！", role);
			}
		}
		return R.error("传递参数为空！");
	}

	@RequiresPermissions("sys:role:delete")
	@RequestMapping(value="deleteObjectByIds",produces="application/json;charset=utf-8")
	public R deleteObjectByIds(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			String ids = jsonObject.getString("ids");
			if(ids != null){
				if(roleService.deleteObjectByIds(ids)) {
					return R.success("修改成功！");
				}
				return R.error("修改失败！", ids);
			}
		}
		return R.error("传递参数为空！");
	}

	@RequiresPermissions("/system/role")
	@RequestMapping(value="getObjectListByPage",produces="application/json;charset=utf-8")
	public R getObjectListByPage(@RequestBody Map<String, Object> params){
		if(params == null){
			params = new HashMap<String, Object>();
		}
		PageInfo<Role> roleList = roleService.getObjectListByPage(params);
		if(roleList != null){
			return R.success(roleList);
		}
		return R.error("未查询到数据！");
	}
}
