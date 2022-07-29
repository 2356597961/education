package com.xiaoliu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoliu.eduservice.client.VodClient;
import com.xiaoliu.eduservice.entity.EduVideo;
import com.xiaoliu.eduservice.mapper.EduVideoMapper;
import com.xiaoliu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-10-17
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //注入远程对象
    @Autowired
    private VodClient vodClient;
    //根据课程ID删除小节,一个课程ID对应多个小节ID，删掉多个视频ID
    //TODO 删除小节，删除对应视频文件
    @Override
    public void removeVideoByCourseId(String courseId) {
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", courseId);
        //查询指定字段的值
        videoWrapper.select("video_source_id");
        //但查出来的依然是个对象
        List<EduVideo> eduVideoList = baseMapper.selectList(videoWrapper);
        //List<EduVideo>变成List<String>
        List<String> videos = new ArrayList<>();
        for (int i = 0; i < eduVideoList.size(); i++) {
            if (!StringUtils.isEmpty(eduVideoList.get(i).getVideoSourceId()))
            videos.add(eduVideoList.get(i).getVideoSourceId());
        }
        //根据多个视频ID删除多个视频
        if (videos.size()>0){
            vodClient.deleteBatch(videos);
        }
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
