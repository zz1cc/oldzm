package com.zm.platform.yw.manage.service.system.code;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.zm.platform.yw.manage.entity.system.code.CodeType;
import com.zm.platform.yw.manage.entity.system.code.CodeValue;

public interface CodeService {

	PageInfo<CodeType> getCodeTypeList(Map<String, Object> params);

	boolean addOneCodeType(CodeType codeType);
	
	boolean updateOneCodeTypeById(CodeType codeType);
	
	boolean deleteCodeTypeByIds(String ids);
	
	PageInfo<CodeValue> getCodeValueListByTypeIdOrTypeName(Map<String, Object> params);
	
	List<CodeValue> getCodeValueListByValid();
	
	void updateCodeListByRedis();

	boolean addOneValueType(CodeValue codeValue);

	boolean updateOneCodeValueById(CodeValue codeValue);
	
	boolean deleteCodeValueByIds(String ids);
}
