package com.xiaoliu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoliu.eduservice.entity.EduSubject;
import com.xiaoliu.eduservice.entity.excel.SubjectData;
import com.xiaoliu.eduservice.service.EduSubjectService;
import com.xiaoliu.servicebase.exceptionhandler.XiaoliuException;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    //因为SubjectExcelListener不能交给spring进行管理，需要自己new,不能注入其他对象
    //不能直接实现数据库操作，所以需要这样做
    public EduSubjectService eduSubjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    //一行一行的读取，没有读取文件头
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        //判断一行是否为空
        if (subjectData==null){
            throw new XiaoliuException(20001,"文件数据为空");
        }
        //一行一行读取,每次读取有两个值，第一个值一级分类，第二值二级分类
        //判断一级分类是否重复,
        EduSubject existOneSubject= this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());
        if (existOneSubject==null){ //存在
            existOneSubject = new EduSubject();
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            existOneSubject.setParentId("0");
            eduSubjectService.save(existOneSubject);
        }

        //添加二级分类
        //判断二级分类是否重复
        //取一级分类的ID值,一级为空，取了添加后的ID值，不为为直接取查询后的值
        String pid=existOneSubject.getId();
        EduSubject existTwoSubject= this.existTwoSubject(eduSubjectService, subjectData.getTwoSubjectName(),pid);
        if (existTwoSubject==null){ //存在
            existTwoSubject = new EduSubject();
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());
            existTwoSubject.setParentId(pid);
            eduSubjectService.save(existTwoSubject);
        }

    }

    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",0);
        EduSubject oneSubject = eduSubjectService.getOne(wrapper);
        return oneSubject;
    }

    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject twoSubject = eduSubjectService.getOne(wrapper);
        return twoSubject;
    }

    //上传文件后做的事
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
