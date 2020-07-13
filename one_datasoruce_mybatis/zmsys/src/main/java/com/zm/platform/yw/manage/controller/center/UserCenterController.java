package com.zm.platform.yw.manage.controller.center;

import com.zm.platform.yw.manage.entity.attachment.Attachment;
import com.zm.platform.yw.manage.entity.system.SysUser;
import com.zm.platform.yw.manage.service.attachment.AttachmentService;
import com.zm.platform.yw.manage.service.system.UserService;
import com.zm.platform.yw.result.R;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/center/",produces="text/plain;charset=utf-8")
public class UserCenterController {
	@Autowired
	private UserService userService;
	@Autowired
	private AttachmentService attachementService;

	@RequestMapping(value="getCurrentUser",produces="application/json;charset=utf-8")
	public R getCurrentUser(){
		SysUser user = userService.getUserByLoginName(
				String.valueOf(SecurityUtils.getSubject().getPrincipal()));
		List<Attachment> attachementList = attachementService.getObjectByUserId(user.getId());
		if(attachementList != null && attachementList.size()>0){
			user.setPhoto(attachementList.get(0).getPath());
		}
		return R.success(user);
	}
}

