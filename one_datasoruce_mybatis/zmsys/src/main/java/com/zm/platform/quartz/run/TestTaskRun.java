package com.zm.platform.quartz.run;

import com.zm.platform.quartz.TaskServer;
import com.zm.platform.common.util.date.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Date;

@TaskServer
public class TestTaskRun {
    private static final Logger LOGGER = LogManager.getLogger(TestTaskRun.class);
    public void testRun(){
        LOGGER.error(" ------------------ start -------------------");
        LOGGER.error(" ------------------ " + DateUtils.formatDateToLine(new Date()) + " -------------------");
        LOGGER.error(" ------------------ end -------------------");
    }
}
