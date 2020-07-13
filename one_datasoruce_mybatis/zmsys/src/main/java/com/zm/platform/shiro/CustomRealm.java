package com.zm.platform.shiro;

import java.util.List;

import com.zm.platform.yw.manage.dao.system.MenuDao;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.zm.platform.common.util.EmptyUtil;
import com.zm.platform.yw.manage.entity.system.Menu;
import com.zm.platform.yw.manage.entity.system.Role;
import com.zm.platform.yw.manage.entity.system.SysUser;
import com.zm.platform.yw.manage.service.system.UserService;
import org.springframework.context.annotation.Lazy;

public class CustomRealm extends AuthorizingRealm {

	//注入的service都要延迟初始化，避免在shiro提前创建导致service事务失效
	@Lazy
    @Autowired
    private UserService userService;
	@Autowired
	private MenuDao menuDao;

    /**
     * 	授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户
    	String name = (String) principalCollection.getPrimaryPrincipal();
		SysUser user = userService.getUserByLoginName(name);

        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		if(user != null){

			List<Role> roleList = user.getRoleList();
			
			if(roleList!=null && roleList.size()>0){
				String[] ids = new String[roleList.size()];
				for (int i = 0; i < roleList.size(); i++) {
					ids[i] = String.valueOf(roleList.get(i).getId());
		            //添加角色
		            simpleAuthorizationInfo.addRole(roleList.get(i).getName());
				}
				
				//角色id查询菜单
				List<Menu> menuList = menuDao.getObjectByRoleIds(ids);
	            //添加菜单权限
	            if(menuList != null){
	                for (Menu menu : menuList) {
	                	if(EmptyUtil.isNotEmpty(menu.getPermission())){
	                        simpleAuthorizationInfo.addStringPermission(menu.getPermission());
	                	}
                        if(EmptyUtil.isNotEmpty(menu.getUrl()) && menu.getUrl().indexOf("/")!=-1){
	                        simpleAuthorizationInfo.addStringPermission(menu.getUrl());
                        }
	                }
	            }
	            //添加权限
	            /*List<Permissions> permissionsList = role.getPermissionsList();
	            if(permissionsList != null){
	                for (Permissions permissions : role.getPermissionsList()) {
	                    simpleAuthorizationInfo.addStringPermission(permissions.getName());
	                }
	            }*/
			}
		}
        return simpleAuthorizationInfo;
    }

    /**
     * 	认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
    	UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
    	String loginname = token.getUsername();
		SysUser user = userService.getUserByLoginName(loginname);
		if (user == null) {
			return null;
		} else {
			String salt = user.getSalt();
			String password = user.getPassword();
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
			return new SimpleAuthenticationInfo(loginname, password, ByteSource.Util.bytes(salt), getName());
        }
    }
}

