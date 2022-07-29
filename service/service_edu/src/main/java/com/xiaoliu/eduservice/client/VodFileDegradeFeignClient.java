package com.xiaoliu.eduservice.client;

import com.xiaoliu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

//交给spring管理
@Component
//实现类实现接口，熔断器判断了出错，就执行该方法
public class VodFileDegradeFeignClient implements VodClient{
    @Override
    public R deleteVideo(String videoId) {
        return R.error().message("删除单个视频出错");
    }

    @Override
    public R deleteBatch(List<String> videoList) {
        return R.error().message("删除多个视频出错了");
    }
}
