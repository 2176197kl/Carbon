package com.carbon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@SpringBootApplication

@ComponentScan({"com.carbon.controller","com.carbon.config"
,"com.carbon.service","com.carbon.util"})
@MapperScan("com.carbon.dao")
public class CarbonApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CarbonApplication.class, args);
    }

}
