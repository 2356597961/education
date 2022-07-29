package com.xiaoliu.orderservice.client;

import com.xiaoliu.commonutils.Course;
import org.springframework.stereotype.Component;

//调用失败就会来到这里
@Component
public class EduClientImpl implements EduClient {
    @Override
    public Course getCourseInfoDto(String courseId) {
        return null;
    }
}
