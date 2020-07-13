package com.zm.platform.quartz.config;

import com.zm.platform.quartz.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 项目启动执行run方法，添加数据库的任务
 */
@Component
//多个用此注解保证顺序，升序执行
@Order(value=1)
public class JobInit implements CommandLineRunner {

    @Autowired
    private QuartzService taskService;

    @Override
    public void run(String... args) throws Exception {
        taskService.taskRunning();
    }
}
