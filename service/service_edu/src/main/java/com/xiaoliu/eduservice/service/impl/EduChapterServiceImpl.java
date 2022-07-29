package com.xiaoliu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoliu.eduservice.entity.EduChapter;
import com.xiaoliu.eduservice.entity.EduCourse;
import com.xiaoliu.eduservice.entity.EduVideo;
import com.xiaoliu.eduservice.entity.chapter.ChapterVo;
import com.xiaoliu.eduservice.entity.chapter.VideoVo;
import com.xiaoliu.eduservice.mapper.EduChapterMapper;
import com.xiaoliu.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoliu.eduservice.service.EduVideoService;
import com.xiaoliu.servicebase.exceptionhandler.XiaoliuException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-10-17
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;
    //根据课程ID查出相应的章节信息合小节信息，然后根据章节的ID把小节封装到相应的章节中
    @Override
    public List<ChapterVo> getChapterViodeoByCourseId(String courseId) {
        //课程大纲，根据课程ID查询
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id",courseId);
        eduChapterQueryWrapper.orderByAsc("gmt_create");
        List<EduChapter> eduChapterList = baseMapper.selectList(eduChapterQueryWrapper);
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id",courseId);
        eduVideoQueryWrapper.orderByAsc("sort");
        List<EduVideo> eduVideoList = eduVideoService.list(eduVideoQueryWrapper);
        //用来存放章节的
        List<ChapterVo> chapterVoList= new ArrayList<>();
        for (EduChapter eduChapter : eduChapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            chapterVoList.add(chapterVo);
            //用来存放小节的
            List<VideoVo> voArrayList= new ArrayList<>();
            for (EduVideo eduVideo : eduVideoList) {
                if (eduChapter.getId().equals(eduVideo.getChapterId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    voArrayList.add(videoVo);
                }
            }
            chapterVo.setChildren(voArrayList);
        }
        return chapterVoList;
    }
    //删除章节的方法
    @Override
    public Boolean deleteChapter(String chapterId) {
        //根据章节Id查询小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(wrapper);
        if (count>0){//存在
            throw new XiaoliuException(20001,"先删除小节");
        }else {//不存在
            int i = baseMapper.deleteById(chapterId);
            return i>0;
        }
    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
