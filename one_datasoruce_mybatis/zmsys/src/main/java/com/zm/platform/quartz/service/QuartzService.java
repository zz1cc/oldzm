package com.zm.platform.quartz.service;

import com.zm.platform.yw.manage.entity.system.task.Task;
import org.quartz.SchedulerException;

public interface QuartzService {

    public void taskRunning();

    /**
     * 添加Job、恢复任务
     * @param job
     */
    public void addJob (Task job) throws SchedulerException;

    /**
     * 修改Job
     * @param job
     */
    public void updateJob (Task job) throws SchedulerException;

    /**
     * 暂停任务、删除任务
     * @param job
     * @throws SchedulerException
     */
    public void pause (Task job) throws SchedulerException;

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
