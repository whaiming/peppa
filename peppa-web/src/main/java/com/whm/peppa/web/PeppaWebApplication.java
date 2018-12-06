package com.whm.peppa.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.whm.peppa.web.db.dao")
public class PeppaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeppaWebApplication.class, args);
    }
}
