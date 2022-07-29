package com.xiaoliu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoliu.eduservice.entity.EduTeacher;
import com.xiaoliu.eduservice.mapper.EduTeacherMapper;
import com.xiaoliu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-10-08
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    @Cacheable(key = "'selectIndexList'",value = "indexteacher")
    public List<EduTeacher> getTeacherIndexlist() {
        QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.orderByDesc("id");
        teacherQueryWrapper.last("limit 4");
        List<EduTeacher> teacherList = baseMapper.selectList(teacherQueryWrapper);
        return teacherList;
    }

    //老师首页的查询方法
    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageParam) {
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();

        //通过sort降序排列
        queryWrapper.orderByAsc("sort");

        //把分页数据封装到pageParam中
        baseMapper.selectPage(pageParam, queryWrapper);
        //总的数据
        List<EduTeacher> records = pageParam.getRecords();
        //当前页
        long current = pageParam.getCurrent();
        //总页数
        long pages = pageParam.getPages();
        //一页的记录数
        long size = pageParam.getSize();
        //总条数
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
}
