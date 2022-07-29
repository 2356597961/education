package com.xiaoliu.eduservice.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoliu.commonutils.R;
import com.xiaoliu.eduservice.entity.EduCourse;
import com.xiaoliu.eduservice.entity.chapter.ChapterVo;
import com.xiaoliu.eduservice.entity.frontvo.CourseWebVo;
import com.xiaoliu.eduservice.entity.vo.CourseInfoVo;
import com.xiaoliu.eduservice.entity.vo.CoursePublishVo;
import com.xiaoliu.eduservice.entity.vo.CourseQuery;
import com.xiaoliu.eduservice.service.EduChapterService;
import com.xiaoliu.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-17
 */
@Api(tags = "课程管理")
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    //填加课程
    @ApiOperation("添加课程")
    @PostMapping("/addCourseInfo")
    public R addCourse(@RequestBody CourseInfoVo courseInfoVo){
        String courseId = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",courseId);
    }
    //根据课程ID查询课程基本信息
    @ApiOperation(value = "根据ID查询课程基本信息")
    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo=courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //修改课程基本信息
    @ApiOperation(value = "修改课程基本信息")
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updataCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程ID确认信息
    @ApiOperation(value = "根据课程ID查询最终发布课程")
    @GetMapping("/getPublishCourseInfo/{courseId}")
    public R getPublishCourseInfo(@PathVariable String courseId){
         CoursePublishVo coursePublishVo=courseService.publishCourseInfo(courseId);
        return R.ok().data("coursePublishVo",coursePublishVo);
    }

    //课程最终发布
    @ApiOperation(value = "最终发布课程")
    @PutMapping("/updateCourseInfo/{courseId}")
    public R  publishCourseById(@PathVariable String courseId){
        courseService. publishCourseById(courseId);
        return R.ok();
    }

    //课程列表分页查询基本实现
    @ApiOperation(value = "分页查询")
    @PostMapping("getCoursePage/{current}/{limit}")
    public R getCoursePage(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) CourseQuery courseQuery){
        return courseService.getCourseList(current,limit,courseQuery);
    }

    //根据课程ID删除课程
    @ApiOperation(value = "根据课程ID删除课程")
    @DeleteMapping("/deleteCourse/{courseId}")
    public R deleteCourse(@PathVariable String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }

    //TODO 课程列表条件分页查询基本实现
}

