package com.xiaoliu.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoliu.commonutils.R;
import com.xiaoliu.eduservice.entity.EduChapter;
import com.xiaoliu.eduservice.entity.EduCourse;
import com.xiaoliu.eduservice.entity.EduTeacher;
import com.xiaoliu.eduservice.service.EduCourseService;
import com.xiaoliu.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Api(tags = "前端老师页面首页")
//@CrossOrigin
@RestController
@RequestMapping("/eduservice/teacherfront")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "分页讲师列表")
    @GetMapping(value = "/pageTeacherList/{page}/{limit}")
    public R pageTeacherList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit){

        Page<EduTeacher> pageParam = new Page<EduTeacher>(page, limit);

        Map<String, Object> map = teacherService.getTeacherFrontList(pageParam);

        //返回分页的所有数据
        return  R.ok().data(map);
    }

    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping(value = "/getCourseByteacherId/{id}")
    public R getById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){

        //查询讲师信息
        EduTeacher teacher = teacherService.getById(id);

        //根据讲师id查询这个讲师的课程列表
        List<EduCourse> courseList = courseService.selectByTeacherId(id);

        return R.ok().data("teacher", teacher).data("courseList", courseList);
    }
}
