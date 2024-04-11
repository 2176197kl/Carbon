package com.carbon;

import com.carbon.fabric.BlockChain;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@SpringBootApplication

@ComponentScan({"com.carbon.controller","com.carbon.config","com.carbon.fabric"
,"com.carbon.service","com.carbon.util"})
@MapperScan("com.carbon.dao")
public class CarbonApplication {
    @Autowired
    BlockChain blockChain;
    public static void main(String[] args) {
        BlockChain.initConn("/root/fabric/scripts/fabric-samples/test-network");
        SpringApplication.run(CarbonApplication.class, args);
    }

}
