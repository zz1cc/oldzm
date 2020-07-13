package com.zm.platform.quartz.job;

import com.zm.platform.quartz.JobStatus;
import com.zm.platform.quartz.TaskServer;
import com.zm.platform.common.util.EmptyUtil;
import com.zm.platform.common.util.SpringUtil;
import com.zm.platform.yw.manage.entity.system.task.Task;
import org.quartz.*;

/**
 * job类
 */
public class QuartzJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Task job = (Task)jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        //包名+类名+方法名
        String invokeName = job.getInvokeName();
        if(EmptyUtil.isNotEmpty_All(invokeName)){
            try {
                int lastPointIndex = invokeName.lastIndexOf(".");
                String beanName = invokeName.substring(0, lastPointIndex);
                String methodName = invokeName.substring(lastPointIndex+1);
                Class<?> clz = Class.forName(beanName);
                Object object = clz.newInstance();
                if (clz.isAnnotationPresent(TaskServer.class)) {
                    //获取方法执行方法
                    clz.getDeclaredMethod(methodName).invoke(object);
                    //只执行一次
                    if(JobStatus.START_ONCE == job.getStatus()){
                        Scheduler scheduler = (Scheduler)SpringUtil.getBean("scheduler");
                        TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(job.getId()));
                        // 停止触发器
                        scheduler.pauseTrigger(triggerKey);
                        // 移除触发器
                        scheduler.unscheduleJob(triggerKey);
                        // 删除任务
                        scheduler.deleteJob(JobKey.jobKey(String.valueOf(job.getId())));
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
