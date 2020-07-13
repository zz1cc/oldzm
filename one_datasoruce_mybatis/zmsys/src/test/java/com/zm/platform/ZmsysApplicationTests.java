package com.zm.platform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;

@SpringBootTest
class ZmsysApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    public void TestspringVersionAndspringBootVersion () {
        //spring版本
        String springVersion = SpringVersion.getVersion();
        //springboot版本
        String springBootVersion = SpringBootVersion.getVersion();
    }

}
