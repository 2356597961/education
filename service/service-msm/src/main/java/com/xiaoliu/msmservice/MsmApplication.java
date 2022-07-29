package com.xiaoliu.msmservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.xiaoliu"}) //swagger的使用等的一些工具类
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //取消自动数据源配置
public class MsmApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsmApplication.class,args);
    }
}
