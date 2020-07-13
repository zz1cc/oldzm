package com.zm.platform.yw.test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zm.platform.yw.manage.entity.system.SysUser;
import com.zm.platform.yw.test.dao.TestDao;


@Service
public class TestServiceImpl implements TestService{
	
	@Autowired(required = true)
	private TestDao testMapper;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	public List<SysUser> listUserBean(String name) {
		List<SysUser> result = new ArrayList<SysUser>();
		//分页插件  https://www.jianshu.com/p/a811a89d1b28
		/*SysUser u1 = new SysUser(1,"andy","Andy","123456");
		SysUser u2 = new SysUser(2,"carl","Carl","123456");
		SysUser u3 = new SysUser(3,"bruce","Bruce","123456");
		SysUser u4 = new SysUser(4,"dolly","Dolly","123456");
		result.add(u1);
		result.add(u2);
		result.add(u3);
		result.add(u4);*/
		return result;
	}

	public SysUser getUserBean(String id) {
		
		//redisTemplate.delete("userBean");
		
		SysUser userBean;
		Long t1 = System.currentTimeMillis();
		Object object = redisTemplate.opsForValue().get("userBean");
		
		if (object != null) {
			userBean = (SysUser)object;
			Long t2 = System.currentTimeMillis();
		} else {
			userBean = testMapper.getUserBean(id);
			Long t2 = System.currentTimeMillis();
			if(userBean != null){
				redisTemplate.opsForValue().set("userBean", userBean);
			}
		}
		return userBean;
	}

	
	@Override
	public List<Map<String, Object>> listProvce() {
		PageHelper.startPage(1,10);
		List<Map<String, Object>> list = testMapper.listProvce();
		PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
		return null;
	}

}
