package com.xiaoliu.eduservice.controller;


import com.xiaoliu.commonutils.R;
import com.xiaoliu.eduservice.entity.subject.OneSubject;
import com.xiaoliu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-15
 */
@Api(tags = "课程管理分类")
@RestController
@RequestMapping("/eduservice/subject")
//@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    //获取上传过来的文件，把文件内容读取出来
    @ApiOperation(value = "excel导入课程")
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file) {
        //上传过来的excel文件
        subjectService.saveSubject(file,subjectService);
        return R.ok();
    }
    //课程分类列表
    @ApiOperation("课程分类列表")
    @GetMapping("/getAllSubject")
    public R getAllSubject(){
        //list集合泛型是一级分类
        List<OneSubject> list=subjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }
}

