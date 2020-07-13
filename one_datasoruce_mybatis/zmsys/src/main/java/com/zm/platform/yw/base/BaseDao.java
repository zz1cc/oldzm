package com.zm.platform.yw.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**类上定义的泛型用于约束类中方法的
 * 参数类型,方法的返回值类型或属性类型*/
public interface BaseDao<T> {
	
	List<T> getObjectList();
	
	//带查询条件
	List<T> getObjectListByPage(Map<String, Object> map);

	T getObjectById(Long id);
	
	boolean saveObject(@Param("entity") T t);
	
	boolean updateObject(@Param("entity") T t);
	
	boolean deleteObjectByIds(String[] ids);
}
