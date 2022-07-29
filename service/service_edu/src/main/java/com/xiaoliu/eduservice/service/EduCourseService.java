package com.xiaoliu.eduservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoliu.commonutils.R;
import com.xiaoliu.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoliu.eduservice.entity.frontvo.CourseQueryVo;
import com.xiaoliu.eduservice.entity.frontvo.CourseWebVo;
import com.xiaoliu.eduservice.entity.vo.CourseInfoVo;
import com.xiaoliu.eduservice.entity.vo.CoursePublishVo;
import com.xiaoliu.eduservice.entity.vo.CourseQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-10-17
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updataCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String courseId);

    void publishCourseById(String courseId);

    R getCourseList(long current, long limit, CourseQuery courseQuery);

    void removeCourse(String courseId);

    List<EduCourse> getCourseIndexList();

    //讲师的页面查出该讲师的所有的课程
    List<EduCourse> selectByTeacherId(String id);

    //课程分页查询
    Map<String, Object> pageListWeb(Page<EduCourse> pageParam, CourseQueryVo courseQuery);

    //前台课程详情信息
    CourseWebVo selectInfoWebById(String courseId);

    /**
     * 更新课程浏览数
     * @param id
     */
    void updatePageViewCount(String id);
}
