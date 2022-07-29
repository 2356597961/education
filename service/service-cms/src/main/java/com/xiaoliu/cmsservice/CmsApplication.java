package com.xiaoliu.cmsservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan({"com.xiaoliu"}) //指定包扫描的位置，全部的R的使用
@MapperScan({"com.xiaoliu.cmsservice.mapper"}) //指定加载的dao
public class CmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class,args);
    }
}
