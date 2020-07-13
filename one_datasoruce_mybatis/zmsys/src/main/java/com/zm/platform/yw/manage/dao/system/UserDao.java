package com.zm.platform.yw.manage.dao.system;

import java.util.List;

import com.zm.platform.yw.base.BaseDao;
import com.zm.platform.yw.manage.entity.system.Role;
import com.zm.platform.yw.manage.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


/**
 * 查询用户信息
 * @author Administrator
 *
 */
@Component
@Mapper
public interface UserDao extends BaseDao<SysUser> {
	
	/**
	 * 根据登录名查询用户
	 * @return
	 */
	SysUser getUserByLoginName(String loginname);
	
	List<Role> getRoleList(Long userId);
}




