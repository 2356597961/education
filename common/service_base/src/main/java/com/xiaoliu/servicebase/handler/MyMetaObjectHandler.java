package com.xiaoliu.servicebase.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //属性名称，不是字段名
        this.setFieldValByName("gmtCreate",LocalDate.now(),metaObject);
        this.setFieldValByName("gmtModified",LocalDate.now(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified",LocalDate.now(),metaObject);
    }
}
