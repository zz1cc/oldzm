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
import com.zm.platform.yw.manage.entity.system.SysUser;
import com.zm.platform.yw.manage.service.system.UserService;

@RestController
@RequestMapping(value="/user/",produces="text/plain;charset=utf-8")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value="getUserByLoginName",produces="application/json;charset=utf-8")
	public R getUserByLoginName(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			String checkedKeys = jsonObject.getString("name");
			SysUser user = userService.getUserByLoginName(checkedKeys);
			if(user != null){
				return R.success(user);
			}
		}
		return R.error("未查询到用户！");
	}

	@RequiresPermissions("/system/user")
	@RequestMapping(value="getObjectListByPage",produces="application/json;charset=utf-8")
	public R getUserList(@RequestBody Map<String, Object> params){
		if(params == null){
			params = new HashMap<String, Object>();
		}
		PageInfo<SysUser> userList = userService.getObjectListByPage(params);
		if(userList != null){
			return R.success(userList);
		}
		return R.error("未查询到用户！");
	}

	@RequestMapping(value="addOrUpdateObject",produces="application/json;charset=utf-8")
	public R addOrUpdateObject(@RequestBody JSONObject jsonObject) {
		if(jsonObject != null) {
			SysUser user = jsonObject.getObject("user", SysUser.class);
			String checkedKeys = jsonObject.getString("checkedKeys");
			if(user != null){
				if(EmptyUtil.isNotEmpty(user.getId())){
					SecurityUtils.getSubject().checkPermissions("sys:user:update");
					if(userService.updateObject(user, checkedKeys)) {
						return R.success("修改成功！", user);
					}else{
						return R.error("修改失败！", user);
					}
				}else{
					SecurityUtils.getSubject().checkPermissions("sys:user:add");
					synchronized (this) {
						if(userService.addObject(user, checkedKeys)) {
							return R.success("新增成功！", user);
						}else{
							return R.error("新增失败！", user);
						}
					}
				}
			}
		}
		return R.error("参数为空！");
	}

	@RequiresPermissions("sys:user:delete")
	@RequestMapping(value="deleteObjectByIds",produces="application/json;charset=utf-8")
	public R deleteObjectByIds(@RequestBody JSONObject jsonObject){
		if(jsonObject != null){
			String ids = jsonObject.getString("ids");
			if(userService.deleteObjectByIds(ids)){
				return R.success("删除成功！");
			}
			return R.error("删除失败！");
		}
		return R.error("参数为空！");
	}

	@RequiresPermissions("sys:user:modifypwd")
	@RequestMapping(value="modifyPassword",produces="application/json;charset=utf-8")
	public R modifyPassword(@RequestBody JSONObject jsonObject){
		if(jsonObject != null){
			SysUser user = jsonObject.getObject("user", SysUser.class);
			if(userService.modifyPassword(user)){
				return R.success("修改密码成功！");
			}
			return R.error("修改密码失败！");
		}
		return R.error("参数为空！");
	}
}
