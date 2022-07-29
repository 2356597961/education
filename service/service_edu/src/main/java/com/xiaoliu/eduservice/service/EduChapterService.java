package com.xiaoliu.eduservice.service;

import com.xiaoliu.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoliu.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-10-17
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterViodeoByCourseId(String courseId);

    //删除章节的方法
    Boolean deleteChapter(String chapterId);

    void removeChapterByCourseId(String courseId);
}
