package com.xiaoliu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoliu.eduservice.entity.EduSubject;
import com.xiaoliu.eduservice.entity.excel.SubjectData;
import com.xiaoliu.eduservice.entity.subject.OneSubject;
import com.xiaoliu.eduservice.entity.subject.TwoSubject;
import com.xiaoliu.eduservice.listener.SubjectExcelListener;
import com.xiaoliu.eduservice.mapper.EduSubjectMapper;
import com.xiaoliu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-10-15
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {
        try{
            //文件输入流
            InputStream inputStream = file.getInputStream();
            //调用方法读取数据，new的对象不能被Spring管理,所以不能注入比如@Autowired
            EasyExcel.read(inputStream, SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //获取一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        //这里的baseMapper是框架已经封装的了，或者直接用this这个this指的是EduSubjectServiceImpl，因为他继承了ServiceImpl
        List<EduSubject> oneSubject = baseMapper.selectList(wrapperOne);

        //获取二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubject = baseMapper.selectList(wrapperTwo);

        //最终返回的集合
        List<OneSubject> finalSubjectList=new ArrayList<>();

        //封装一级分类
        for (int i = 0; i < oneSubject.size(); i++) {
            EduSubject eduSubject = oneSubject.get(i);
            OneSubject oneSubject1 = new OneSubject();
//            oneSubject1.setId(eduSubject.getId());
//            oneSubject1.setTitle(eduSubject.getTitle());
            //简化写法
            BeanUtils.copyProperties(eduSubject,oneSubject1);
            finalSubjectList.add(oneSubject1);
            //封装二级分类,创建集合存二级分类
            List<TwoSubject> twoFinalSubjectList=new ArrayList<>();
            for (int i1 = 0; i1 < twoSubject.size(); i1++) {
                //获取每个二级分类
                EduSubject eduSubject1 = twoSubject.get(i1);
                //判断每个二级分类是属于哪个一级分类
                if (eduSubject1.getParentId().equals(eduSubject.getId())) {
                    //把eduSubject1的值放到twoSubject中去
                    TwoSubject twoSubject1 = new TwoSubject();
                    BeanUtils.copyProperties(eduSubject1,twoSubject1);
                    twoFinalSubjectList.add(twoSubject1);
                }
            }
            //把twoFinalSubjectList的值放到oneSubject1中去
            oneSubject1.setChildren(twoFinalSubjectList);
        }
        return finalSubjectList;
    }
}
