package com.zm.platform.yw.test.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.zm.platform.yw.manage.entity.system.SysUser;



/**
 * @Repository需要在Spring中配置扫描地址，然后生成Dao层的Bean才能被注入到Service层中
 * @Mapper不需要配置扫描地址，通过xml里面的namespace里面的接口地址，生成了Bean后注入到Service层中。
 * @author Administrator
 *
 */
@Component
@Mapper
public interface TestDao {
	
	SysUser getUserBean(String id);
	
	List<Map<String, Object>> listProvce();
}
