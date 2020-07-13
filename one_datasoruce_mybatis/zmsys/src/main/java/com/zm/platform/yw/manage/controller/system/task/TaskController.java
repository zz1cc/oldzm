package com.zm.platform.yw.manage.controller.system.task;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zm.platform.yw.manage.entity.system.task.Task;
import com.zm.platform.yw.manage.service.system.task.TaskService;
import com.zm.platform.yw.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/task/",produces="text/plain;charset=utf-8")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@RequestMapping(value="getObjectListByPage",produces="application/json;charset=utf-8")
	public R getObjectListByPage(@RequestBody Map<String, Object> params){
		if(params == null){
			params = new HashMap<String, Object>();
		}
		PageInfo<Task> pageList = taskService.getObjectListByPage(params);
		if(pageList != null){
			return R.success(pageList);
		}
		return R.error("传递参数为空！");
	}

	@RequestMapping(value="saveObject",produces="application/json;charset=utf-8")
	public R saveObject(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			Task task = jsonObject.getObject("task", Task.class);
			if(taskService.saveObject(task)){
				return R.success();
			}
			return R.error("保存失败！");
		}
		return R.error("传递参数为空！");
	}

	@RequestMapping(value="updateObject",produces="application/json;charset=utf-8")
	public R updateObject(@RequestBody JSONObject jsonObject){
		if(jsonObject != null) {
			Task task = jsonObject.getObject("task", Task.class);
			if(taskService.updateObject(task)){
				return R.success();
			}
			return R.error("修改失败！");
		}
		return R.error("传递参数为空！");

	}

	@RequestMapping(value="deleteObjectByIds",produces="application/json;charset=utf-8")
	public R deleteObjectByIds(@RequestBody JSONObject jsonObject){
		if(jsonObject != null){
			String ids = jsonObject.getString("ids");
			if(taskService.deleteObjectByIds(ids)){
				return R.success();
			}
			return R.error("删除失败！");
		}
		return R.error("传递参数为空！");
	}

    @RequestMapping(value="startOrPause",produces="application/json;charset=utf-8")
    public R startOrPause(@RequestBody JSONObject jsonObject){
        if(jsonObject != null) {
            Task task = jsonObject.getObject("task", Task.class);
            if(taskService.startOrPause(task)){
				return R.success();
            }
			return R.error("删除失败！");
        }
		return R.error("传递参数为空！");
    }

    @RequestMapping(value="startOne",produces="application/json;charset=utf-8")
    public R startOne(@RequestBody JSONObject jsonObject){
        if(jsonObject != null) {
            Task task = jsonObject.getObject("task", Task.class);
            if(taskService.startOne(task)){
				return R.success();
            }
			return R.error("删除失败！");
        }
		return R.error("传递参数为空！");
    }


}

