package com.zm.platform.yw.manage.controller.system.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zm.platform.common.util.CodeUtils;
import com.zm.platform.common.util.EmptyUtil;
import com.zm.platform.yw.result.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zm.platform.yw.manage.entity.system.code.CodeType;
import com.zm.platform.yw.manage.entity.system.code.CodeValue;
import com.zm.platform.yw.manage.service.system.code.CodeService;

@RestController
@RequestMapping(value="/code/",produces="text/plain;charset=utf-8")
public class CodeController {

	@Autowired
	private CodeService codeService;

	/** 类型 **/
	@RequiresPermissions("/system/code")
	@RequestMapping(value="getCodeTypeList",produces="application/json;charset=utf-8")
	public R getCodeTypeList(@RequestBody Map<String, Object> params){
		if(params == null){
			params = new HashMap<String, Object>();
		}
		PageInfo<CodeType> pageinfo = codeService.getCodeTypeList(params);
		if(pageinfo != null){
			return R.success(pageinfo);
		}
		return R.error("未查询到信息！");
	}

	//增加类型
	@RequiresPermissions("sys:codetype:add")
	@RequestMapping(value="addOneCodeType",produces="application/json;charset=utf-8")
	public R addOneCodeType(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			CodeType codeType = jsonObject.getObject("codeType", CodeType.class);
			if(codeType != null){
				if(codeService.addOneCodeType(codeType)) {
					//更新redis的值
					codeService.updateCodeListByRedis();
					return R.success("新增字典成功！", codeType);
				} else {
					return R.error("新增字典失败！", codeType);
				}
			}
		}
		return R.error("传递参数为空！");
	}
	
	//修改类型
	@RequiresPermissions("sys:codetype:update")
	@RequestMapping(value="updateOneCodeTypeById",produces="application/json;charset=utf-8")
	public R updateOneCodeTypeById(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			CodeType codeType = jsonObject.getObject("codeType", CodeType.class);
			synchronized (this) {
				if(codeService.updateOneCodeTypeById(codeType)) {
					//更新redis的值
					codeService.updateCodeListByRedis();
					return R.success("修改字典成功！", codeType);
				} else {
					return R.error("修改字典失败！", codeType);
				}
			}
		}
		return R.error("传递参数为空！");
	}

	//删除类型
	@RequiresPermissions("sys:codetype:delete")
	@RequestMapping(value="deleteCodeTypeByIds",produces="application/json;charset=utf-8")
	public R deleteCodeTypeByIds(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			String ids = jsonObject.getString("ids");
			if(codeService.deleteCodeTypeByIds(ids)) {
				//更新redis的值
				codeService.updateCodeListByRedis();
				return R.success("删除字典成功！", ids);
			} else {
				return R.error("删除字典失败！", ids);
			}
		}
		return R.error("传递参数为空！");
	}

	/** 值    **/
	//查询类型值列表带分页
	@RequestMapping(value="getCodeValueListByTypeIdOrTypeName",produces="application/json;charset=utf-8")
	public R getCodeValueListByTypeIdOrTypeName(@RequestBody Map<String, Object> params){
		if(params == null){
			params = new HashMap<String, Object>();
		}
		PageInfo<CodeValue> pageinfo = codeService.getCodeValueListByTypeIdOrTypeName(params);
		if(pageinfo != null){
			return R.success(pageinfo);
		}
		return R.error("未查询到信息！");
	}
	
	//查询字典类型值列表(一个类型)
	@RequestMapping(value="getCodeValueList",produces="application/json;charset=utf-8")
	public R getCodeValueListByOneType(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			String typeName = jsonObject.getString("typeName");
			if(EmptyUtil.isNotEmpty(typeName)) {
				List<CodeValue> list = CodeUtils.getCodeValueListByOneType(typeName);
				return R.success(list);
			}
		}
		return R.error("未查询到信息！");
	}
	
	//查询字典类型值列表(多个类型)
	@RequestMapping(value="getCodeValueListByMoreType",produces="application/json;charset=utf-8")
	public R getCodeValueListByMoreType(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			String typeName = jsonObject.getString("typeName");
			try {
				Map<String, List<CodeValue>> map = CodeUtils.getCodeValueListByMoreType(typeName);
				if(map != null) {
					return R.success(map);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return R.error("未查询到信息！", e);
			}
		}
		return R.error("未查询到信息！");
	}
	
	//增加类型值
	@RequiresPermissions("sys:codevalue:add")
	@RequestMapping(value="addOneValueType",produces="application/json;charset=utf-8")
	public R addOneValueType(@RequestBody JSONObject jsonObject){
		if(jsonObject != null){
			CodeValue codeValue = jsonObject.getObject("codeValue", CodeValue.class);
			if(codeService.addOneValueType(codeValue)) {
				//更新redis的值
				codeService.updateCodeListByRedis();
				return R.success("新增记录成功！", codeValue);
			} else {
				return R.error("新增记录失败！", codeValue);
			}
		}
		return R.error("传递参数为空！");
	}
	
	//修改类型值
	@RequiresPermissions("sys:codevalue:update")
	@RequestMapping(value="updateOneCodeValueById",produces="application/json;charset=utf-8")
	public R updateOneCodeValueById(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			CodeValue codevalue = jsonObject.getObject("codeValue", CodeValue.class);
			if(codeService.updateOneCodeValueById(codevalue)) {
				//更新redis的值
				codeService.updateCodeListByRedis();
				return R.success("修改记录成功！", codevalue);
			}
			else {
				return R.error("修改记录失败！", codevalue);
			}
		}
		return R.error("传递参数为空！");
	}
	
	//删除类型值
	@RequiresPermissions("sys:codevalue:delete")
	@RequestMapping(value="deleteCodeValueByIds",produces="application/json;charset=utf-8")
	public R deleteCodeValueByIds(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			String ids = jsonObject.getString("ids");
			if(codeService.deleteCodeValueByIds(ids)) {
				//更新redis的值
				codeService.updateCodeListByRedis();
				return R.success("删除记录成功！");
			} else {
				return R.error("删除记录失败！", ids);
			}
		}
		return R.error("传递参数为空！");
	}
}
