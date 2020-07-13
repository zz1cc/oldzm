package com.zm.platform.yw.manage.service.system;

import java.util.*;

import com.zm.platform.common.exception.runtime.CurrentException;
import com.zm.platform.common.exception.runtime.ParamIsNullException;
import com.zm.platform.common.util.EmptyUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zm.platform.yw.manage.dao.system.UserDao;
import com.zm.platform.yw.manage.dao.system.UserRoleDao;
import com.zm.platform.yw.manage.entity.system.Role;
import com.zm.platform.yw.manage.entity.system.SysUser;
import com.zm.platform.yw.manage.entity.system.UserRole;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao userRoleDao;

	public PageInfo<SysUser> getObjectListByPage(Map<String, Object> params) {
		if(params == null){
			throw new ParamIsNullException("参数为空");
		}
		Object pageNum = params.get("pageNum");
		Object pageSize = params.get("pageSize");
		if(EmptyUtil.isNotEmpty_All(pageNum, pageSize)){
			PageHelper.startPage((Integer)pageNum, (Integer)pageSize);
		}
		List<SysUser> list = userDao.getObjectListByPage(params);
		PageInfo<SysUser> pageInfo = new PageInfo<SysUser>(list);
		return pageInfo;
	}

	/**
	 * 修改用户信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateObject(SysUser user, String checkedKeys) {
		if(user == null){
			throw new ParamIsNullException("参数为空");
		}
		try {

			SysUser u = userDao.getObjectById(user.getId());
			if(u == null){
				throw new CurrentException("未查询到用户，请刷新后重试");
			}
			if(!user.getLoginName().equals(u.getLoginName())){
				throw new CurrentException("请勿修改用户名");
			}
			if(!userDao.updateObject(user)){
				throw new CurrentException("修改用户信息失败");
			}
			//删除用户角色
			List<Role> roleList = u.getRoleList();
			if(roleList!=null && roleList.size()>0){
				String[] ids = new String[]{String.valueOf(user.getId())};
				int row = userRoleDao.deleteObjectByUserIds(ids);
				if(row <= 0){
					throw new CurrentException("删除用户角色失败");
				}
			}

			//增加用户角色
			if(EmptyUtil.isNotEmpty(checkedKeys)){
				String[] menuIds = checkedKeys.split(",");
				//角色菜单数组
				List<UserRole> list = new ArrayList<>();
				UserRole userRole = null;
				for(String id: menuIds){
					userRole = new UserRole();
					userRole.setUserId(user.getId());
					userRole.setRoleId(Long.valueOf(id));
					list.add(userRole);
				}
				if(!userRoleDao.saveMoreObject(list)){
					throw new CurrentException("新增角色菜单失败");
				}
			}
			return true;
		} catch (CurrentException c){
			throw new CurrentException(c.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CurrentException("操作用户信息失败");
		}
	}

	/**
	 * 修改密码
	 */
	public boolean modifyPassword(SysUser user) {
		if(user == null){
			throw new ParamIsNullException("参数为空");
		}
		Long id = user.getId();
		String password = user.getPassword();
		if(EmptyUtil.isHasEmpty(id, password)){
			throw new CurrentException("参数为空");
		}
		SysUser u = userDao.getObjectById(id);
		if(u == null){
			throw new CurrentException("未查询到用户，请刷新后重试");
		}

		String salt = UUID.randomUUID().toString();
		user.setSalt(salt);
		//加密次数与配置类要一致
		password = new SimpleHash("MD5", password, salt, 2).toHex();
		user.setPassword(password);

		if(!userDao.updateObject(user)){
			throw new CurrentException("修改用户信息失败");
		}
		return true;
	}

	/**
	 * 新增用户信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addObject(SysUser user, String checkedKeys) {
		if(user == null){
			throw new ParamIsNullException("参数为空");
		}
		try {

			SysUser u = userDao.getUserByLoginName(user.getLoginName());
			if(EmptyUtil.isNotEmpty(u)){
				throw new CurrentException("此登陆名已存在");
			}
			String loginName = user.getLoginName();
			String password = user.getPassword();
			if(EmptyUtil.isHasEmpty(loginName, password)){
				throw new CurrentException("登陆名或密码不能为空");
			}
			//盐
			String salt = UUID.randomUUID().toString();
			user.setSalt(salt);
			//加密次数与配置类要一致
			password = new SimpleHash("MD5", password, salt, 2).toHex();
			user.setPassword(password);

			if(!userDao.saveObject(user)){
				throw new CurrentException("新增用户信息失败");
			}
			//增加用户角色
			if(EmptyUtil.isNotEmpty(checkedKeys)){
				String[] menuIds = checkedKeys.split(",");
				//角色菜单数组
				List<UserRole> list = new ArrayList<>();
				UserRole userRole = null;
				for(String id: menuIds){
					userRole = new UserRole();
					userRole.setUserId(user.getId());
					userRole.setRoleId(Long.valueOf(id));
					list.add(userRole);
				}
				if(!userRoleDao.saveMoreObject(list)){
					throw new CurrentException("新增角色菜单失败");
				}
			}
			return true;
		} catch (CurrentException c){
			throw new CurrentException(c.getMessage());
		}  catch (Exception e) {
			e.printStackTrace();
			throw new CurrentException("操作用户信息失败");
		}
	}

	@Override
	public SysUser getUserByLoginName(String loginname) {
		return userDao.getUserByLoginName(loginname);
	}

	@Override
	public List<Role> getRoleList(Long userId) {
		if(userId == null){
			throw new ParamIsNullException("参数为空");
		}
		return userDao.getRoleList(userId);
	}

	/**
	 * 批量删除
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteObjectByIds(String ids) {
		if(ids == null){
			throw new ParamIsNullException("参数为空");
		}
		String[] idArr = ids.split(",");
		if(new ArrayList<String>(Arrays.asList(idArr)).indexOf("1") != -1){
			throw new CurrentException("无法删除管理员！");
		}
		try {
			//删除用户角色表
			userRoleDao.deleteObjectByUserIds(idArr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParamIsNullException("删除用户角色失败");
		}
		//删除用户
		if(!userDao.deleteObjectByIds(idArr)){
			throw new ParamIsNullException("删除用户失败");
		}
		return true;
	}

}
