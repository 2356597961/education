package com.xiaoliu.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoliu.commonutils.R;
import com.xiaoliu.eduservice.entity.EduTeacher;
import com.xiaoliu.eduservice.entity.vo.TeacherQuery;
import com.xiaoliu.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-08
 */
@Api(tags="讲师管理")
@RestController
//@CrossOrigin //加注解解决跨域问题,或者加网关
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {

    //地址http://localhost:8001/education/teacher/findAll
    //把service注入
    @Autowired
    private EduTeacherService teacherService;

    //查询讲师表所有的数据
    //rest风格
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("/findAll")
    public R findAllTeacher(){
        //调用service的方法实现查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    //逻辑删除讲师的方法,{id}表示id需要通过路径进行传递,required表示是否必须携带
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(  @ApiParam(name = "id", value = "讲师ID", required = true)
                                   @PathVariable String  id){
        boolean flag = teacherService.removeById(id);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //分页查询讲师的方法,current当前页，limit每页的记录数
    @ApiOperation(value = "老师成员分页查询")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageListTeacher(@ApiParam(name = "current",value = "当前页",required = true)
                             @PathVariable long current,
                             @ApiParam(name = "limit",value = "当前页总数",required = true)
                             @PathVariable long limit){
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        //调用方法实现分页,调用方法时，底层封装，把分页所有数据封装到pageTeacher对象里面
        teacherService.page(pageTeacher,null);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//每页数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //条件查询带分页的方法,这里的teacherQuery可以不加@RequestBody(required =false)（json数据格式）,false为不必须带参数,改为@PostMapping
    //否则直接用@GetMapping,不用加@RequestBody
    //@RequestBody请求json数据，@ResponseBody返回json数据
    @ApiOperation(value = "老师分页查询根据条件")
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@ApiParam(name = "current",value = "当前页",required = true)
                                  @PathVariable long current,
                                  @ApiParam(name = "limit",value = "当前页总数",required = true)
                                  @PathVariable long limit,
                                  //使用前端同样的方法，他会根据传来的body是否为空，进行判断
                                  @RequestBody(required=false) TeacherQuery teacherQuery){
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //调用方法实现条件查询分页
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件查询，动态sql
        //判断条件值是否为空，如果不为空拼接条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)){
            //构建条件
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }
        //降序排序
        wrapper.orderByDesc("gmt_create");
        teacherService.page(pageTeacher,wrapper);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> rows = pageTeacher.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",rows);
    }

    //添加讲师接口的方法
    @ApiOperation(value = "添加讲师")
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation(value = "老师ID查询")
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@ApiParam(name = "id",value = "老师ID",required = true)
                        @PathVariable String id){
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    @ApiOperation(value = "修改讲师")
    @PostMapping("/updateTacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean flag = teacherService.updateById(eduTeacher);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

