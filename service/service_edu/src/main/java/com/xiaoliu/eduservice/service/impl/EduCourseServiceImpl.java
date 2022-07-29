package com.xiaoliu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoliu.commonutils.R;
import com.xiaoliu.eduservice.entity.EduCourse;
import com.xiaoliu.eduservice.entity.EduCourseDescription;
import com.xiaoliu.eduservice.entity.frontvo.CourseQueryVo;
import com.xiaoliu.eduservice.entity.frontvo.CourseWebVo;
import com.xiaoliu.eduservice.entity.vo.CourseInfoVo;
import com.xiaoliu.eduservice.entity.vo.CoursePublishVo;
import com.xiaoliu.eduservice.entity.vo.CourseQuery;
import com.xiaoliu.eduservice.mapper.EduCourseMapper;
import com.xiaoliu.eduservice.service.EduChapterService;
import com.xiaoliu.eduservice.service.EduCourseDescriptionService;
import com.xiaoliu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoliu.eduservice.service.EduVideoService;
import com.xiaoliu.servicebase.exceptionhandler.XiaoliuException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-10-17
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //注入课程描述服务类
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    //小节service
    @Autowired
    private EduVideoService eduVideoService;
    //章节service
    @Autowired
    private EduChapterService eduChapterService;
    //描述service
    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    //添加课程（包括课程信息）
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //向课程表加入数据
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        boolean resultCourseInfo = this.save(eduCourse);
        if (!resultCourseInfo){
            throw new XiaoliuException(20001,"添加课程失败");
        }
        String courseId=eduCourse.getId();
        //向课程描述表添加数据
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescription.setId(courseId);
        boolean save = courseDescriptionService.save(eduCourseDescription);
        if (!save) {
            throw new XiaoliuException(20001, "添加课程描述失败");
        }
        return courseId;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //根据课程ID查询课程信息
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo=new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        //课程ID
        String id = eduCourse.getId();
        //查询描述表
        EduCourseDescription eduCourseDescription = courseDescriptionService.getById(id);
        courseInfoVo.setDescription(eduCourseDescription.getDescription());
        return courseInfoVo;
    }

    @Override
    public void updataCourseInfo(CourseInfoVo courseInfoVo) {
        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int i = baseMapper.updateById(eduCourse);
        if (i<=0){
            throw new XiaoliuException(20001,"修改课程信息失败");
        }
        //修改课程描述表
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        boolean b = courseDescriptionService.updateById(eduCourseDescription);
        if (!b){
            throw new XiaoliuException(20001,"修改描述失败");
        }
    }

    @Override
    public CoursePublishVo publishCourseInfo(String courseId) {
        //调用mapper,必须是自己baseMapper才能调用自己写的方法,但有一个问题无法加载到配置文件.xml需要把它放在resource
        //文件夹下，或者直接把他放在相应的target目录下
        //推荐使用第三种方式，使用配置文件完成，(1)pom.xml,(2)application.properties文件配置
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(courseId);
        return publishCourseInfo;
    }

    @Override
    public void publishCourseById(String courseId) {
        EduCourse course=new EduCourse();
        course.setId(courseId);
        course.setStatus("Normal");
        baseMapper.updateById(course);
    }

    //分页查询
    @Override
    public R getCourseList(long current, long limit, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        String name = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        if (!StringUtils.isEmpty(name)){
            wrapper.like("title",name);
        }
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        Page<EduCourse> coursePage = new Page(current,limit);
        this.page(coursePage, wrapper);
        long total = coursePage.getTotal();
        List<EduCourse> courseList = coursePage.getRecords();
        return R.ok().data("total",total).data("rows",courseList);
    }

    //删除课程表及其以下的字段
    @Override
    public void removeCourse(String courseId) {
        //根据课程ID删除小节
        eduVideoService.removeVideoByCourseId(courseId);
        //根据课程ID删除章节
        eduChapterService.removeChapterByCourseId(courseId);
        //根据课程ID删除描述
        courseDescriptionService.removeById(courseId);
        //根据课程ID删除课程本身
        int i = baseMapper.deleteById(courseId);
        if (i<=0){
            throw new XiaoliuException(20001,"删除失败");
        }
    }

    @Override
    @Cacheable(key = "'selectIndexList'",value = "indexcourse")
    public List<EduCourse> getCourseIndexList() {
        //查询出前8条热门课程
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("id");
        courseQueryWrapper.last("limit 8");
        List<EduCourse> courseList = baseMapper.selectList(courseQueryWrapper);
        return courseList;
    }

    //根据老师id查出课程信息
    @Override
    public List<EduCourse> selectByTeacherId(String id) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<EduCourse>();

        queryWrapper.eq("teacher_id", id);
        //按照最后更新时间倒序排列
        queryWrapper.orderByDesc("gmt_modified");

        List<EduCourse> courses = baseMapper.selectList(queryWrapper);
        return courses;
    }

    //前台课程分页
    @Override
    public Map<String, Object> pageListWeb(Page<EduCourse> pageParam, CourseQueryVo courseQuery) {
        //查询条件
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        //判断有没有，有的就判断加上查询条件
        if (!StringUtils.isEmpty(courseQuery.getSubjectParentId())) { //一级分类
            queryWrapper.eq("subject_parent_id", courseQuery.getSubjectParentId());
        }

        if (!StringUtils.isEmpty(courseQuery.getSubjectId())) { //二级分类
            queryWrapper.eq("subject_id", courseQuery.getSubjectId());
        }

        if (!StringUtils.isEmpty(courseQuery.getBuyCountSort())) { //关注度
            queryWrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(courseQuery.getGmtCreateSort())) { //最新时间，降序排列
            queryWrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseQuery.getPriceSort())) { //价格
            queryWrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageParam, queryWrapper);

        //记录
        List<EduCourse> records = pageParam.getRecords();
        //当前页
        long current = pageParam.getCurrent();
        //总页数
        long pages = pageParam.getPages();
        //当前页得记录数
        long size = pageParam.getSize();
        //查询条件的总记录数
        long total = pageParam.getTotal();
        //下一页
        boolean hasNext = pageParam.hasNext();
        //上一页
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    //获得前端页面中的课程详情信息的
    @Override
    public CourseWebVo selectInfoWebById(String courseId) {
        this.updatePageViewCount(courseId);
        return baseMapper.selectInfoWebById(courseId);
    }

    //每次点击前端页面详情页面是，就会使浏览数加1
    @Override
    public void updatePageViewCount(String id) {
        EduCourse course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
    }
}
