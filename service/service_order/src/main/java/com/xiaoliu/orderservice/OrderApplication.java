package com.xiaoliu.orderservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.xiaoliu.orderservice.mapper") //作用是作指定要变成实现类的接口所在的包，
// 然后包下面的所有接口在编译之后都会生成相应的实现类
@ComponentScan("com.xiaoliu") //工具类：如果你有一些bean所在的包，不在main 的包及其下级包，
// 那么你需要手动加上@ComponentScan注解并指定那个bean所在的包
@EnableDiscoveryClient //nacos注册
@EnableFeignClients  //谁调用其他服务的就要调用加这个注解
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
