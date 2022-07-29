package com.xiaoliu.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 */

@SpringBootApplication
@EnableFeignClients //feign的远程调用
@EnableDiscoveryClient //进行nacos注册的注解
@ComponentScan(basePackages = {"com.xiaoliu"})
public class EduApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class,args);
    }
}
