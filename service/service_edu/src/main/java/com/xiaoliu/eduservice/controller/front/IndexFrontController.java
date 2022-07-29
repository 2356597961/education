package com.xiaoliu.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoliu.commonutils.R;
import com.xiaoliu.eduservice.entity.EduCourse;
import com.xiaoliu.eduservice.entity.EduTeacher;
import com.xiaoliu.eduservice.service.EduCourseService;
import com.xiaoliu.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("/eduservice/indexfront")
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    //查询前8条热门课程，查询出前4条名师
    @GetMapping("/index")
    public R index(){
        //查询出前8条热门课程
        List<EduCourse> courseList = courseService.getCourseIndexList();
        //查询出前4条老师记录回来
        List<EduTeacher> teacherList = teacherService.getTeacherIndexlist();
        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }
}
