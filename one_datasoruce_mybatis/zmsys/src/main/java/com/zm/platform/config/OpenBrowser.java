package com.zm.platform.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OpenBrowser implements CommandLineRunner{

    private Logger logger = LoggerFactory.getLogger(OpenBrowser.class);

	@Value("${server.port}")
	private String port;

    @Value("${spring.auto.openurl}")
    private boolean isOpen;

    public void run(String... args) throws Exception {
        if(isOpen){
            String cmd = "cmd /c start http://localhost:" + port + "/";
            logger.info(cmd);
            Runtime run = Runtime.getRuntime();
            try{
                //run.exec(cmd);
                logger.debug("启动浏览器打开项目成功");
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }
}
