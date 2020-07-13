package com.zm.platform.yw.manage.service.system;

import java.util.ArrayList;
import java.util.List;

import com.zm.platform.common.exception.runtime.ParamIsNullException;
import com.zm.platform.common.util.CodeUtils;
import com.zm.platform.common.util.EmptyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.platform.yw.manage.dao.system.MenuDao;
import com.zm.platform.yw.manage.dao.system.RoleMenuDao;
import com.zm.platform.yw.manage.dao.system.UserDao;
import com.zm.platform.yw.manage.entity.system.Menu;
import com.zm.platform.yw.manage.entity.system.Role;
import com.zm.platform.yw.manage.entity.system.SysUser;

@Service
public class MenuServiceImpl implements MenuService{

	@Autowired
	private MenuDao menuDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleMenuDao roleMenuDao;

	//新增
	public boolean saveObject(Menu menu){
		if(menu == null){
			throw new ParamIsNullException("参数为空");
		}
		//新增菜单
		if(!menuDao.saveObject(menu)){
			throw new ParamIsNullException("新增菜单失败");
		}
		List<Menu> menuList = menuDao.getObjectList();

		if(menuList!=null && menuList.size()>0){
			//是否有父菜单
			List<Menu> fatherMenu = getFatherMenu(menu, menuList);
			//将所有含有此父菜单的角色菜单设置为半选状态
			if(fatherMenu!=null && fatherMenu.size()>0){
				String[] ids = new String[fatherMenu.size()];
				for (int i = 0; i < fatherMenu.size(); i++) {
					ids[i] = String.valueOf(fatherMenu.get(i).getId());
				}
				if(!roleMenuDao.updateMoreObject(ids)){
					throw new ParamIsNullException("操作失败");
				}
			}
		}
		return true;
	}

	//修改
	public boolean updateObject(Menu menu){
		if(menu == null || EmptyUtil.isEmpty_All(menu.getId())){
			throw new ParamIsNullException("参数为空");
		}
		return menuDao.updateObject(menu);
	}

	//删除
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteObjectByIds(String ids){
		if(ids == null){
			throw new ParamIsNullException("参数为空");
		}
		List<Menu> menuList = menuDao.getObjectList();
		if(menuList == null || menuList.size() == 0){
			throw new ParamIsNullException("删除失败");
		}
		String[] idArr = ids.split(",");

		List<String> idList = new ArrayList<>();
		for(String s: idArr){
			//子菜单id
			List<String> list = this.getSonMenuId(Long.valueOf(s), menuList);
			if(list != null){
				idList.addAll(list);
			}
			//自身id
			idList.add(s);
		}
		if(idList.size() > 0){
			String[] s = new String[idList.size()];
			s = idList.toArray(s);
			//删除菜单
			if(!menuDao.deleteObjectByIds(s)){
				throw new ParamIsNullException("删除菜单失败");
			}
			//删除角色菜单
			try {
				roleMenuDao.deleteObjectByMenuIds(s);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ParamIsNullException("删除菜单失败");
			}
		}
		return true;
	}

	/**
	 * 查询当前用户的菜单
	 */
	@Override
	public List<Menu> getAsideMenuList() {
		try {
			//当前登录名
			Subject subject = SecurityUtils.getSubject();
			Object loginName = subject.getPrincipal();
			//当前用户信息
			SysUser user = userDao.getUserByLoginName((String)loginName);
			if(EmptyUtil.isNotEmpty(user)){
				//用户包含的角色
				List<Role> roleList = user.getRoleList();
				if(roleList!=null && roleList.size()>0){
					String[] ids = new String[roleList.size()];
					for (int i = 0; i < roleList.size(); i++) {
						ids[i] = String.valueOf(roleList.get(0).getId());
					}
					//角色id查询菜单
					List<Menu> menuList = menuDao.getObjectByRoleIds(ids);
					//处理菜单
					if(menuList != null && menuList.size() > 0){
						//一级菜单
						List<Menu> mainMenuList = new ArrayList<>();
						//其他菜单、按钮
						List<Menu> otherMenuList = new ArrayList<>();
						for(Menu menu: menuList){
							if(EmptyUtil.isNullOrEmpty(menu.getParentId())){
								mainMenuList.add(menu);
							}else{
								otherMenuList.add(menu);
							}
						}
						for(Menu mainMenu: mainMenuList) {
							List<Menu> list = getSonMenu(mainMenu.getId(), otherMenuList);
							mainMenu.setChildren(list);
						}
						return mainMenuList;
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Menu> getObjectByRoleIds(String ids) {
		return menuDao.getObjectByRoleIds(ids.split(","));
	}

	/**
	 * 获取所有菜单
	 * @return
	 */
	@Override
	public List<Menu> getAllMenuList() {
		try {
			List<Menu> menuList = menuDao.getObjectList();
			if(menuList != null && menuList.size() > 0){
				for(Menu menu: menuList){
					Integer type = menu.getType();
					if(EmptyUtil.isNotEmpty(type)) {
						menu.setTypeName(CodeUtils.getCodeValueName("sys_menuType", type));
					}
				}

				//一级菜单
				List<Menu> mainMenuList = new ArrayList<>();
				//其他菜单、按钮
				List<Menu> otherMenuList = new ArrayList<>();
				for(Menu menu: menuList){
					if(EmptyUtil.isNullOrEmpty(menu.getParentId())){
						mainMenuList.add(menu);
					}else{
						otherMenuList.add(menu);
					}
				}
				for(Menu mainMenu: mainMenuList) {
					List<Menu> list = getSonMenu(mainMenu.getId(), otherMenuList);
					mainMenu.setChildren(list);
				}
				return mainMenuList;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 	递归设置子菜单
	 * @param id
	 * @param menuList
	 * @return
	 */
	public List<Menu> getSonMenu(Long id, List<Menu> menuList) {
		if(EmptyUtil.isHasEmpty(id, menuList)) {
			return null;
		}
		List<Menu> sonMenuList = new ArrayList<Menu>();
		for(Menu menu: menuList) {
			if(id.equals(menu.getParentId())) {
				List<Menu> list = getSonMenu(menu.getId(), menuList);
				menu.setChildren(list);
				sonMenuList.add(menu);
			}
		}
		if(sonMenuList.size() == 0) {
			return null;
		}
		return sonMenuList;
	}

	/**
	 * 递归获取所有子菜单id
	 * @param id
	 * @param menuList
	 * @return
	 */
	public List<String> getSonMenuId(Long id, List<Menu> menuList) {
		if(EmptyUtil.isHasEmpty(id, menuList)) {
			return null;
		}
		List<String> idList = new ArrayList<>();
		for(Menu menu: menuList) {
			if(id.equals(menu.getParentId())) {
				List<String> list = getSonMenuId(menu.getId(), menuList);
				if(list != null){
					idList.addAll(list);
				}
				idList.add(String.valueOf(menu.getId()));
			}
		}
		if(idList.size() == 0) {
			return null;
		}
		return idList;
	}

	/**
	 * 	递归获取所有父菜单
	 * @param id
	 * @param menuList
	 * @return
	 */
	public List<Menu> getFatherMenu(Menu son, List<Menu> menuList) {
		if(EmptyUtil.isHasEmpty(son, menuList)) {
			return null;
		}
		if(EmptyUtil.isHasEmpty(son.getParentId())){
			return null;
		}
		List<Menu> fatherMenuList = new ArrayList<Menu>();
		for(Menu menu: menuList) {
			if(son.getParentId().equals(menu.getId())) {
				fatherMenuList.add(menu);
				List<Menu> list = getFatherMenu(menu, menuList);
				if(list != null){
					fatherMenuList.addAll(list);
				}
			}
		}
		return fatherMenuList;
	}
}