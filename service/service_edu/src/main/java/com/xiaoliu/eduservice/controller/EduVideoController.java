package com.xiaoliu.eduservice.controller;


import com.xiaoliu.commonutils.R;
import com.xiaoliu.eduservice.client.VodClient;
import com.xiaoliu.eduservice.entity.EduVideo;
import com.xiaoliu.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-17
 */
@Api(tags = "小节管理")
@RestController
@RequestMapping("/eduservice/video")
//@CrossOrigin
public class EduVideoController {

    //注入vodClient
    @Autowired
    private VodClient vodClient;
    @Autowired
    private EduVideoService eduVideoService;

    @ApiOperation(value = "添加小节")
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        boolean flag = eduVideoService.save(eduVideo);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }
    //TODO 后面的这个方法需要完善,删除小节时候，同时把里面的视频删除(涉及跨服务操作，使用微服务解决)
    @ApiOperation(value = "删除小节")
    @DeleteMapping("/deleteVideo/{videoId}")
    public R deleteVideo(@PathVariable String videoId){
        EduVideo video = eduVideoService.getById(videoId);
        String videoSourceId = video.getVideoSourceId();
        //删除视频
        //为避免删除数据库出错,判断视频ID是否为空
        if (!StringUtils.isEmpty(videoSourceId)){
            //删除视频
            vodClient.deleteVideo(videoSourceId);
        }
        //删除小节
        boolean flag = eduVideoService.removeById(videoId);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //TODO 修改小节

    //TODO 根据ID获取小节

}

