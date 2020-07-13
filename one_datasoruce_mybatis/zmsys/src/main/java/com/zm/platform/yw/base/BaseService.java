package com.zm.platform.yw.base;

import java.util.List;
import java.util.Map;

/**类上定义的泛型用于约束类中方法的
 * 参数类型,方法的返回值类型或属性类型*/
public interface BaseService<T> {
	int insertObject(T t);
	
	List<Map<String,Object>> findObjects(T entity);

	Map<String,Object> findObjectById(Integer id);
	
	int validById(String[] ids, Integer valid);
	
	int save(T t);
	
	int save(Map<String, Object> map);
	
	int updateObject(T t);
	
	int deletObject(Integer id);
}
