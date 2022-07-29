package com.xiaoliu.eduservice.controller;


import com.xiaoliu.commonutils.R;
import com.xiaoliu.eduservice.entity.EduChapter;
import com.xiaoliu.eduservice.entity.chapter.ChapterVo;
import com.xiaoliu.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "章节管理")
@RestController
//@CrossOrigin
@RequestMapping("/eduservice/chapter")
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    //返回大纲列表
    @ApiOperation(value = "获取所有的章节")
    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){
        List<ChapterVo> list=chapterService.getChapterViodeoByCourseId(courseId);
        return R.ok().data("list",list);
    }
    //添加章节信息
    @ApiOperation(value = "添加章节")
    @PostMapping("/addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }

    //根据章节ID查询
    @ApiOperation(value = "根据章节Id查询")
    @GetMapping("/getChapterById/{chapterId}")
    public R getChapterById(@PathVariable String chapterId){
        EduChapter chapter = chapterService.getById(chapterId);
        return R.ok().data("chapter",chapter);
    }

    //修改章节信息
    @ApiOperation(value = "修改章节信息")
    @PostMapping("/updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    //删除章节
    @ApiOperation(value = "根据章节ID删除")
    @DeleteMapping("/deleteChapter/{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){
        //章节有小节，所以先要删掉小节在删章节，(或者如果存在小节就不能删除章节)
        Boolean flag = chapterService.deleteChapter(chapterId);
        if (flag) {
            return R.ok();
        }else {
            return R.error();
        }
    }
}

