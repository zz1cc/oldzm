package com.zm.platform.yw.manage.service.system.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zm.platform.common.exception.runtime.CurrentException;
import com.zm.platform.common.exception.runtime.ParamIsNullException;
import com.zm.platform.common.util.EmptyUtil;
import com.zm.platform.common.util.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zm.platform.yw.manage.dao.system.code.CodeDao;
import com.zm.platform.yw.manage.entity.system.code.CodeType;
import com.zm.platform.yw.manage.entity.system.code.CodeValue;

@Service
public class CodeServiceImpl implements CodeService {

	@Autowired
	private CodeDao codeDao;
	
	/**
	 * 查询字典类型列表，可分页
	 */
	public PageInfo<CodeType> getCodeTypeList(Map<String, Object> params) {
		if(EmptyUtil.isNullOrEmpty(params)){
			throw new ParamIsNullException("参数为空");
		}
		Object pageNum = params.get("pageNum");
		Object pageSize = params.get("pageSize");
		if(EmptyUtil.isNotEmpty_All(pageNum, pageSize)){
			PageHelper.startPage((Integer)pageNum, (Integer)pageSize);
		}
		List<CodeType> list = codeDao.getCodeTypeList(params);
		if(list != null && list.size()>0) {
			PageInfo<CodeType> pageInfo = new PageInfo<CodeType>(list);
			return pageInfo;
		}
		return null;
	}

	/**
	 * 新增字典类型
	 */
	public synchronized boolean addOneCodeType(CodeType codeType) {
		if(codeType == null){
			throw new ParamIsNullException("参数为空");
		}
		
		if(EmptyUtil.isNotEmpty(codeDao.getCodeTypeByType(codeType))) {
			throw new CurrentException("此类型已存在！");
		}
		String userName = RedisUtil.get("userName").toString();
		codeType.setCreateUser(userName);
		codeType.setModifyUser(userName);
		return codeDao.addOneCodeType(codeType);
	}

	/**
	 * 修改字典类型
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean updateOneCodeTypeById(CodeType codeType) {
		if(codeType == null || EmptyUtil.isEmpty_All(codeType.getId())){
			throw new ParamIsNullException("参数为空");
		}
		
		CodeType type = codeDao.getCodeTypeByType(codeType);
		if(EmptyUtil.isNotEmpty(type) && !type.getId().equals(codeType.getId())) {
			throw new CurrentException("此类型已存在！");
		}
		
		//修改类型
		boolean f1 = codeDao.updateOneCodeTypeById(codeType);
		if(!f1) {
			throw new CurrentException("修改字典类型失败！");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("typeId", codeType.getId());
		List<CodeValue> list = codeDao.getCodeValueListByTypeIdOrTypeName(params);
		if(list!=null && list.size()>0) {
			//修改类型对应的值
			CodeValue codeValue = new CodeValue();
			codeValue.setTypeId(codeType.getId());
			if(EmptyUtil.isNotEmpty(codeType.getType())) {
				codeValue.setTypeName(codeType.getType());
			}
			boolean f2 = codeDao.updateOneCodeValueByTypeId(codeValue);
			if(!f2) {
				throw new CurrentException("修改字典值失败！");
			}
		}
		return true;
	}

	/**
	 * 删除字典类型
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteCodeTypeByIds(String ids) {
		if(ids == null){
			throw new ParamIsNullException("参数为空");
		}
		String[] idArr = ids.split(",");
		//删除一个或多个类型
		if(!codeDao.deleteCodeTypeByIds(idArr)) {
			throw new CurrentException("删除字典失败！");
		}
		String[] valueIdArr = null;
		List<CodeValue> list = codeDao.getCodeValueListByTypeIds(idArr);
		if(list!=null && list.size()>0) {
			valueIdArr = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				valueIdArr[i] = String.valueOf(list.get(i).getId());
			}
			if(!codeDao.deleteCodeValueByIds(valueIdArr)) {
				throw new CurrentException("删除值失败！");
			}
		}
		return true;
	}
	
	/**
	 * 	根据类型id或者类型name查询字典值列表，可分页
	 */
	public PageInfo<CodeValue> getCodeValueListByTypeIdOrTypeName(Map<String, Object> params) {
		if(params == null){
			throw new ParamIsNullException("参数为空");
		}
		if(EmptyUtil.isEmpty_All(params.get("typeName"), params.get("typeId"))){
			throw new ParamIsNullException("参数为空");
		}
		Object pageNum = params.get("pageNum");
		Object pageSize = params.get("pageSize");
		if(EmptyUtil.isNotEmpty_All(pageNum, pageSize)){
			PageHelper.startPage((Integer)pageNum, (Integer)pageSize);
		}
		List<CodeValue> list = codeDao.getCodeValueListByTypeIdOrTypeName(params);
		if(list != null && list.size()>0) {
			PageInfo<CodeValue> pageInfo = new PageInfo<CodeValue>(list);
			return pageInfo;
		}
		return null;
	}
	
	/**
	 * 	查询所有有效的类型值
	 */
	public List<CodeValue> getCodeValueListByValid() {
		List<CodeValue> resultList = (List<CodeValue>)RedisUtil.get("codeValueList");
		if(EmptyUtil.isNullOrEmpty(resultList)) {
			resultList = codeDao.getCodeValueListByValid();
			RedisUtil.set("codeValueList", resultList);
		}
		return resultList;
	}
	
	/**
	 * 	更新redis字典值列表
	 */
	public void updateCodeListByRedis() {
		RedisUtil.del("codeValueList");
		this.getCodeValueListByValid();
	}
	
	/**
	 * 新增字典值
	 */
	public boolean addOneValueType(CodeValue codeValue) {
		if(codeValue == null){
			throw new ParamIsNullException("参数为空");
		}
		return codeDao.addOneValueType(codeValue);
	}
	
	/**
	 * 修改字典值
	 */
	public boolean updateOneCodeValueById(CodeValue codeValue) {
		if(codeValue == null || EmptyUtil.isEmpty_All(codeValue.getId())){
			throw new ParamIsNullException("参数为空");
		}
		return codeDao.updateOneCodeValueById(codeValue);
	}
	
	/**
	 * 删除字典值
	 */
	public boolean deleteCodeValueByIds(String ids) {
		if(ids == null){
			throw new ParamIsNullException("参数为空");
		}
		return codeDao.deleteCodeValueByIds(ids.split(","));
	}
}
