package com.xiaoliu.ucenterservice;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient //nacos注册
@ComponentScan({"com.xiaoliu"})
@MapperScan({"com.xiaoliu.ucenterservice.mapper"}) //扫描mapper的包，xml使用到
public class UcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterApplication.class,args);
    }
}
