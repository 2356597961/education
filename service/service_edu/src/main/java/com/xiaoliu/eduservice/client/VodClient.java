package com.xiaoliu.eduservice.client;

import com.xiaoliu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
//fallback表示的是实现类，熔断器配置
@FeignClient(value = "service-vod",fallback = VodFileDegradeFeignClient.class) //远程调用的名字
@Component //表示交给spring容器管理
public interface VodClient {
    //远程调用
    @DeleteMapping("/eduvod/vod//deleteVideo/{videoId}") //要写绝对路径
    public R deleteVideo(@PathVariable("videoId") String videoId);

    //定义删除多个视频的方法,这个是参数类型的，不是路径上的
    @DeleteMapping("/eduvod/vod/delete-batch")
    public R deleteBatch(@RequestParam("/videoList") List<String> videoList);
}
