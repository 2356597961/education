package com.xiaoliu.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoliu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-10-08
 */
public interface EduTeacherService extends IService<EduTeacher> {

    //首页就是进去的页面
    List<EduTeacher> getTeacherIndexlist();
    //老师首页
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageParam);
}
