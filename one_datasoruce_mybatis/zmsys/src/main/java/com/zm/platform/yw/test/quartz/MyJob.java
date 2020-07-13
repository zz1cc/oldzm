package com.zm.platform.yw.test.quartz;

import com.alibaba.fastjson.JSONObject;
import org.quartz.*;

@DisallowConcurrentExecution
public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        System.out.println(JSONObject.toJSONString(jobDataMap));
    }
}
