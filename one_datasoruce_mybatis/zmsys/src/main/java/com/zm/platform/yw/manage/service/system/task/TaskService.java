package com.zm.platform.yw.manage.service.system.task;

import com.github.pagehelper.PageInfo;
import com.zm.platform.yw.manage.entity.system.task.Task;
import org.quartz.SchedulerException;

import java.util.Map;

public interface TaskService {

    /**
     * 可分页获取Job
     */
    PageInfo<Task> getObjectListByPage(Map<String, Object> params);

    /**
     * 获取一个Job
     */
    Task getObjectById(Long id);

    /**
     * 添加Job
     * @param job
     */
    boolean saveObject(Task job);

    /**
     * 修改JOb
     * @param job job
     */
    boolean updateObject(Task job);

    /**
     * 单个或批量删除JOb
     */
    boolean deleteObjectByIds(String ids);

    /**
     *
     */
    boolean startOrPause(Task job);

    /**
     *
     * @param job
     * @return
     */
    boolean startOne(Task job);

    /**
     * 开启所有任务
     * @throws SchedulerException
     */
    public void startAllJob () throws SchedulerException;

    /**
     * 停止所有任务
     * @throws SchedulerException
     */
    public void pauseAllJob () throws SchedulerException;

}
