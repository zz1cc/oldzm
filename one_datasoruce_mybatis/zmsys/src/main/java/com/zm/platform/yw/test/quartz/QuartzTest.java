package com.zm.platform.yw.test.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTest {
    public static void main(String[] args) {
        try {
            //获取调度器
            Scheduler scheduled = StdSchedulerFactory.getDefaultScheduler();
            //添加监听器
            scheduled.getListenerManager().addJobListener(new MyJobListener());
            //
            JobDetail job = JobBuilder
                    //绑定一个job
                    .newJob(MyJob.class)
                    //job名称和分组
                    .withIdentity("MyJob", "group")
                    //job数据
                    .usingJobData("lll", "222")
                    .build();
            //简单触发器
            Trigger trigger = TriggerBuilder.newTrigger()
                    //触发器名称和组名
                    .withIdentity("myTrigger", "group")
                    //执行间隔
                    .withSchedule(
                            /**
                             * 两种触发器
                             * SimpleTrigger 仅当需调度一次或者以固定时间间隔周期执行调度
                             * CronTrigger  通过Cron表达式定义出各种复杂时间规则的调度，在线生成语句：https://qqe2.com/cron
                             */
                            CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
                    .build();
            //组装Job和Trigger
            scheduled.scheduleJob(job, trigger);
            scheduled.start();
            scheduled.pauseJob(JobKey.jobKey("MyJob", "group"));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
