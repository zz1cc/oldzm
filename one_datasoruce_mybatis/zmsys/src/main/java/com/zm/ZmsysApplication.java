package com.zm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"com.zm.platform.yw.*.dao.**"})
public class ZmsysApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZmsysApplication.class, args);
    }

}
