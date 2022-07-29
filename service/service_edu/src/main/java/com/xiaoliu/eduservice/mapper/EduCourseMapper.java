package com.xiaoliu.eduservice.mapper;

import com.xiaoliu.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoliu.eduservice.entity.frontvo.CourseWebVo;
import com.xiaoliu.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-10-17
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    //最终发布
    public CoursePublishVo getPublishCourseInfo(String courseId);

    //前端课程详情页面
    CourseWebVo selectInfoWebById(String courseId);
}
