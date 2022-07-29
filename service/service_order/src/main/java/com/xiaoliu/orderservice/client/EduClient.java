package com.xiaoliu.orderservice.client;

import com.xiaoliu.commonutils.Course;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Component
@FeignClient(name = "service-edu",fallback = EduClientImpl.class)
public interface EduClient {

    //根据课程id查询课程详情
    @GetMapping("/eduservice/coursefront/getDto/{courseId}")
    public Course getCourseInfoDto(@PathVariable String courseId);
}
