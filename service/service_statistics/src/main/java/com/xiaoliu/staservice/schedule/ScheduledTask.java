package com.xiaoliu.staservice.schedule;

import com.xiaoliu.staservice.service.StatisticsDailyService;
import com.xiaoliu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

//目的自己自动写入统计表中的
@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService dailyService;

    /**
     * 测试
     * 每五秒执行一次
     */
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1() {
//        System.out.println("*********++++++++++++*****执行了");
//    }

    /**
     * 每天凌晨1点执行定时 定时任务这里只能写6位，七位报错，年是指定当前这一年的
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void task() {
        //获取上一天的日期
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        dailyService.createStatisticsByDay(day);

    }
}
