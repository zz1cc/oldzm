package com.zm.platform.yw.manage.dao.system.code;

import java.util.List;
import java.util.Map;

import com.zm.platform.yw.manage.entity.system.code.CodeType;
import com.zm.platform.yw.manage.entity.system.code.CodeValue;

public interface CodeDao {

	/**
	 * 	通过类型名称查询一条记录
	 * @param codeType
	 * @return
	 */
	CodeType getCodeTypeByType(CodeType codeType);
	
	/**
	 * 查询字典类型列表，可分页
	 * @param params
	 * @return
	 */
	List<CodeType> getCodeTypeList(Map<String, Object> params);
	
	boolean addOneCodeType(CodeType codeType);
	
	boolean updateOneCodeTypeById(CodeType codeType);
	
	boolean deleteCodeTypeByIds(String[] ids);

	/**
	 * 根据typeid或者typename查询字典值列表，可分页
	 * @param params
	 * @return
	 */
	List<CodeValue> getCodeValueListByTypeIdOrTypeName(Map<String, Object> params);
	
	/**
	 * 根据多个类型id查询所有字典值 
	 * @param params
	 * @return
	 */
	List<CodeValue> getCodeValueListByTypeIds(String[] typeIds);
	
	/**
	 *	查询所有有效的代码值
	 * @return
	 */
	List<CodeValue> getCodeValueListByValid();
	
	boolean addOneValueType(CodeValue codeValue);
	
	boolean updateOneCodeValueById(CodeValue codeValue);
	
	boolean updateOneCodeValueByTypeId(CodeValue codeValue);
	
	boolean deleteCodeValueByIds(String[] ids);
	
	boolean deleteCodeValueBytypeIds(String[] typeIds);
}
