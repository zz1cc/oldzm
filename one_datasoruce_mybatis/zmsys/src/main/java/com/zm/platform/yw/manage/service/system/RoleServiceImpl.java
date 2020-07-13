package com.zm.platform.yw.manage.service.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zm.platform.common.exception.runtime.CurrentException;
import com.zm.platform.common.exception.runtime.ParamIsNullException;
import com.zm.platform.common.util.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zm.platform.yw.manage.dao.system.RoleDao;
import com.zm.platform.yw.manage.dao.system.RoleMenuDao;
import com.zm.platform.yw.manage.dao.system.UserRoleDao;
import com.zm.platform.yw.manage.entity.system.Menu;
import com.zm.platform.yw.manage.entity.system.Role;
import com.zm.platform.yw.manage.entity.system.RoleMenu;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private RoleMenuDao roleMenuDao;
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Override
	public PageInfo<Role> getObjectListByPage(Map<String, Object> params) {
		if(params == null){
			throw new ParamIsNullException("参数为空");
		}
		Object pageNum = params.get("pageNum");
		Object pageSize = params.get("pageSize");
		if(EmptyUtil.isNotEmpty_All(pageNum, pageSize)){
			PageHelper.startPage((Integer)pageNum, (Integer)pageSize);
		}
		List<Role> list = roleDao.getObjectListByPage(params);
		if(list != null && list.size()>0) {
			PageInfo<Role> pageInfo = new PageInfo<Role>(list);
			return pageInfo;
		}
		return null;
	}

	@Override
	public Role getObjectById(Long id) {
		if(EmptyUtil.isNotEmpty(id)){
			throw new ParamIsNullException("参数为空");
		}
		return roleDao.getObjectById(id);
	}

	/**
	 * 新增或修改角色信息
	 * @checkedKeys 角色新的菜单id，逗号分隔
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addOrUpdateObject(Role role, String checkedKeys, String halfCheckKeys) {
		if(EmptyUtil.isNullOrEmpty(role)){
			throw new ParamIsNullException("参数为空");
		}
		
		boolean b = false;
		
		try {

			if(EmptyUtil.isNotEmpty(role.getId())){
				//修改
				//查询是否有角色
				Role r = roleDao.getObjectById(role.getId());
				if(EmptyUtil.isNullOrEmpty(r)){
					throw new ParamIsNullException("未查询到此角色");
				}
				//1.修改角色信息
				b = roleDao.updateObject(role);
				if(!b){
					throw new CurrentException("更新角色信息失败");
				}
				//2.如果存在菜单就删除此角色以前的菜单
				List<Menu> menuList = r.getMenuList();
				if(menuList!=null && menuList.size()>0){
					int row = roleMenuDao.deleteObjectByRoleIds(new String[]{String.valueOf(role.getId())});
					if(row <= 0){
						throw new CurrentException("删除角色菜单失败");
					}
				}
			}else{
				//新增
				b = roleDao.saveObject(role);				
				if(!b){
					throw new CurrentException("新增角色失败");
				}
			}
			
			//3.增加角色的新菜单
			if(!EmptyUtil.isEmpty_All(checkedKeys, halfCheckKeys)){
				//角色菜单数组
				List<RoleMenu> list = new ArrayList<>();
				RoleMenu roleMenu = null;
				//全选中菜单
				if(EmptyUtil.isNotEmpty(checkedKeys)){
					String[] menuIds = checkedKeys.split(",");
					for(String id: menuIds){
						roleMenu = new RoleMenu();
						roleMenu.setMenuId(Long.valueOf(id));
						roleMenu.setRoleId(role.getId());
						roleMenu.setHalfCheck(0);
						list.add(roleMenu);
					}
				}
				//半选中菜单
				if(EmptyUtil.isNotEmpty(halfCheckKeys)){
					String[] menuIds = halfCheckKeys.split(",");
					for(String id: menuIds){
						roleMenu = new RoleMenu();
						roleMenu.setMenuId(Long.valueOf(id));
						roleMenu.setRoleId(role.getId());
						roleMenu.setRoleId(role.getId());
						roleMenu.setHalfCheck(1);
						list.add(roleMenu);
					}
				}
				b = roleMenuDao.saveMoreObject(list);
				if(!b){
					throw new CurrentException("新增角色菜单失败");
				}
			}
			return true;
		} catch (CurrentException c){
			throw new CurrentException(c.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CurrentException("操作角色信息失败");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteObjectByIds(String ids) {
		if(ids == null){
			throw new ParamIsNullException("参数为空");
		}
		
		try {

			//删除角色
			if(!roleDao.deleteObjectByIds(ids.split(","))){
				throw new ParamIsNullException("删除角色失败");
			}
			//删除角色菜单表
			roleMenuDao.deleteObjectByRoleIds(ids.split(","));
			//删除用户角色
			userRoleDao.deleteObjectByRoleIds(ids.split(","));
			
		} catch (CurrentException c){
			throw new CurrentException(c.getMessage());
		}  catch (Exception e) {
			e.printStackTrace();
			throw new ParamIsNullException("删除角色失败");
		}
		
		return true;
	}

}
