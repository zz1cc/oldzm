package com.zm.platform.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.zm.platform.yw.manage.entity.system.code.CodeValue;
import com.zm.platform.yw.manage.service.system.code.CodeService;

@Component
public class CodeUtils {
	
	@Autowired
	private static CodeService codeService;
	
	@Autowired
	public void setCodeService(CodeService codeService) {
		CodeUtils.codeService = codeService;
	}
	
	/**
	 * 	查询单个字典类型的所有字典值
	 * @param codeTypeName
	 * @return
	 */
	public static List<CodeValue> getCodeValueListByOneType(String typeName){
		if(EmptyUtil.isNotEmpty(typeName)) {
			//查询所有字典值
			List<CodeValue> codeValueList = codeService.getCodeValueListByValid();
			if(codeValueList != null && codeValueList.size() > 0) {
				//返回的字典值list
				List<CodeValue> result = new ArrayList<CodeValue>();
				//遍历所有字典值
				for(CodeValue codeValue: codeValueList) {
					//匹配参数传的类型
					if(typeName.equals(codeValue.getTypeName())) {
						result.add(codeValue);
					}
				}
				if(result.size() > 0) {
					return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * 	查询多个字典类型的所有字典值
	 * @param codeTypeName
	 * @return
	 */
	public static Map<String, List<CodeValue>> getCodeValueListByMoreType(String codeTypeNames){
		if(EmptyUtil.isNotEmpty(codeTypeNames)) {
			Map<String, List<CodeValue>> resultMap = new HashMap<String, List<CodeValue>>();
			List<CodeValue> codeValueList = codeService.getCodeValueListByValid();
			if(codeValueList != null) {
				for(String codeTypeName: codeTypeNames.split(",")) {
					List<CodeValue> result = new ArrayList<CodeValue>();
					for(CodeValue codeValue: codeValueList) {
						if(codeTypeName.equals(codeValue.getTypeName())) {
							result.add(codeValue);
						}
					}
					if(result.size() > 0) {
						resultMap.put(codeTypeName, result);
					}
				}
			}
			if(resultMap.size() > 0) {
				return resultMap;
			}
		}
		return null;
	}
	
	/**
	 * 	查询codevalue的名称
	 * @param codeTypeName 字典类型名称name
	 * @param value 字典值的value
	 * @return
	 */
	public static String getCodeValueName(String codeTypeName, Object value){
		if(EmptyUtil.isNotEmpty_All(codeTypeName, value)) {
			List<CodeValue> codeValueList = codeService.getCodeValueListByValid();
			if(codeValueList != null) {
				for(CodeValue codeValue: codeValueList) {
					if(codeValue.getTypeName().equals(codeTypeName) && codeValue.getValue().equals(value)) {
						return codeValue.getName();
					}
				}
			}
		}
		return null;
	}
}
