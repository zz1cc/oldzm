package com.zm.platform.quartz.service;

import com.zm.platform.quartz.JobStatus;
import com.zm.platform.quartz.job.QuartzJob;
import com.zm.platform.yw.manage.entity.system.task.Task;
import com.zm.platform.yw.manage.service.system.task.TaskServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuartzServiceImpl implements QuartzService {

    private static final Logger LOGGER = LogManager.getLogger(QuartzServiceImpl.class);

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private TaskServiceImpl scheduleJobService;

    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(Task job) {
        return JobKey.jobKey(String.valueOf(job.getId()));
    }
    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(Task job) {
        return TriggerKey.triggerKey(String.valueOf(job.getId()));
    }

    public void taskRunning(){
        List<Task> list = new ArrayList<>();
        //list.forEach(this:: addjob);
    }

    /**
     * 添加、恢复Job
     * @param job
     */
    public void addJob (Task job) throws SchedulerException {
        //创建job信息
        JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class).withIdentity(getJobKey(job)).build();
        //job参数
        jobDetail.getJobDataMap().put("scheduleJob", job);
        //创建定时cron表达式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        //创建trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(job)).withSchedule(scheduleBuilder).build();
        //调度任务
        scheduler.scheduleJob(jobDetail, trigger);
        // 如果是立即运行则首先触发一次任务
        if(job.getStatus() == JobStatus.START_NOW){
            //立即执行
            scheduler.triggerJob(getJobKey(job), jobDetail.getJobDataMap());
        }
    }

    /**
     * 修改任务
     * @param job
     * @throws SchedulerException
     */
    public void updateJob(Task job) throws SchedulerException {
        this.pause(job);
        this.addJob(job);
    }

    /**
     * 暂停任务、删除任务
     * @param job
     * @throws SchedulerException
     */
    public void pause(Task job) throws SchedulerException {
        //停止触发器
        scheduler.pauseTrigger(getTriggerKey(job));
        //移除触发器
        scheduler.unscheduleJob(getTriggerKey(job));
        //删除任务
        scheduler.deleteJob(getJobKey(job));
    }

    /**
     * 恢复任务
     * @param job
     * @throws SchedulerException
     */
    public void resume(Task job) throws SchedulerException {
        scheduler.resumeJob(getJobKey(job));
    }

    /**
     * 开启所有任务
     * @throws SchedulerException
     */
    public void startAllJob () throws SchedulerException {
        scheduler.start();
    }

    /**
     * 停止所有任务
     * @throws SchedulerException
     */
    public void pauseAllJob () throws SchedulerException {
        scheduler.standby();
    }

}
