package com.xiaoliu.staservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.xiaoliu.staservice.mapper")
@ComponentScan("com.xiaoliu")
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling //定时执行某些方法的作用，这里就是可以生成前一天的统计信息，如果要指定某一天或者今天的就要看了
// 就要手动的生成,还是要看自己设置的多少时间执行一次
public class StaApplication {
    public static void main(String[] args) {
        SpringApplication.run(StaApplication.class,args);
    }
}
