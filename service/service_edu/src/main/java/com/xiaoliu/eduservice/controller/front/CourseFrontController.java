package com.xiaoliu.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoliu.commonutils.Course;
import com.xiaoliu.commonutils.JwtUtils;
import com.xiaoliu.commonutils.R;
import com.xiaoliu.eduservice.client.OrderClient;
import com.xiaoliu.eduservice.entity.EduCourse;
import com.xiaoliu.eduservice.entity.chapter.ChapterVo;
import com.xiaoliu.eduservice.entity.frontvo.CourseQueryVo;
import com.xiaoliu.eduservice.entity.frontvo.CourseWebVo;
import com.xiaoliu.eduservice.entity.vo.CourseInfoVo;
import com.xiaoliu.eduservice.service.EduChapterService;
import com.xiaoliu.eduservice.service.EduCourseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//@CrossOrigin
@RestController
@RequestMapping("/eduservice/coursefront")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    @ApiOperation(value = "分页课程列表")
    @PostMapping(value = "/getTeacherForntInfo/{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
            @RequestBody(required = false) CourseQueryVo courseQuery){
        Page<EduCourse> pageParam = new Page<EduCourse>(page, limit);
        Map<String, Object> map = courseService.pageListWeb(pageParam, courseQuery);
        //前端取值直接response.data.data
        return  R.ok().data(map);
    }

    @ApiOperation(value = "根据ID查询课程")
    @GetMapping(value = "/getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId, HttpServletRequest request){

        //查询课程信息和讲师信息
        CourseWebVo courseWebVo = courseService.selectInfoWebById(courseId);

        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = chapterService.getChapterViodeoByCourseId(courseId);

        //避免出错，避免查数据库报空指针错误，或者避免了又一次调用这个没有用的判断是否购买
        if (StringUtils.isEmpty(JwtUtils.getMemberIdByJwtToken(request))){
            return R.error().code(28004).message("请先登录");
        }
        //远程调用，判断课程是否被购买
        boolean buyCourse = orderClient.isBuyCourse(JwtUtils.getMemberIdByJwtToken(request),courseId);

        return R.ok().data("course", courseWebVo).data("chapterVoList", chapterVoList).data("isbuyCourse",buyCourse);
    }

    //根据课程id查询课程信息
    @GetMapping("getDto/{courseId}")
    public Course getCourseInfoDto(@PathVariable String courseId) {
        CourseInfoVo courseInfo = courseService.getCourseInfo(courseId);
        Course course = new Course();
        BeanUtils.copyProperties(courseInfo,course);
        return course;
    }
}
