package com.xiaoliu.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoliu.staservice.client.UcenterClient;
import com.xiaoliu.staservice.entity.StatisticsDaily;
import com.xiaoliu.staservice.mapper.StatisticsDailyMapper;
import com.xiaoliu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-03
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {


    @Autowired
    private UcenterClient ucenterClient;
    //根据时间查询当前的注册人数
    @Override
    public void createStatisticsByDay(String day) {
        //删除已存在的统计对象
        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(dayQueryWrapper);


        //获取统计信息，微服务使用的目的，在实际开发中无权力使用他人的数据库，人家提供接口，我们远超调用，存在自己的数据库中
        Integer registerNum = (Integer) ucenterClient.registerCount(day).getData().get("countRegister"); //注册人数
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO  //登录人数，用redis计数器存起来然后取出
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO  //每日视频播放数 不太会
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO  //每日新增课程数  不太会

        //创建统计对象
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);

        baseMapper.insert(daily);
    }

    //生成图表
    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {
        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        //选择要查询出来的字段
        dayQueryWrapper.select(type, "date_calculated");
        //这里可以使用大于小于，但这里
        dayQueryWrapper.between("date_calculated", begin, end);

        List<StatisticsDaily> dayList = baseMapper.selectList(dayQueryWrapper);

        Map<String, Object> map = new HashMap<>(); //list集合变成json数组；对象，map集合变成json对象
        List<Integer> dataList = new ArrayList<Integer>();
        List<String> dateList = new ArrayList<String>();

        map.put("dataList", dataList);
        map.put("dateList", dateList);


        //遍历封装到数组中
        for (int i = 0; i < dayList.size(); i++) {
            StatisticsDaily daily = dayList.get(i);
            dateList.add(daily.getDateCalculated());
            switch (type) {
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        return map;
    }
}
