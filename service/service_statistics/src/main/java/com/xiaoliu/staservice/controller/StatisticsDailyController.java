package com.xiaoliu.staservice.controller;


import com.xiaoliu.commonutils.R;
import com.xiaoliu.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-03
 */
//@CrossOrigin
@RestController
@RequestMapping("/staservice/statistics")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService dailyService;

    //调用会员登录的模块进行当前的注册人数统计
    @PostMapping("{day}")
    public R createStatisticsByDate(@PathVariable String day) {
        dailyService.createStatisticsByDay(day);
        return R.ok();
    }

    //生成图表，x坐标和y坐标都是数组，用map封装服务层返回来的数据
    @GetMapping("show-chart/{begin}/{end}/{type}")
    public R showChart(@PathVariable String begin,@PathVariable String end,@PathVariable String type){
        Map<String, Object> map = dailyService.getChartData(begin, end, type);
        return R.ok().data(map);
    }

}

