package com.xiaoliu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.xiaoliu.commonutils.R;
import com.xiaoliu.servicebase.exceptionhandler.XiaoliuException;
import com.xiaoliu.vod.service.VodService;
import com.xiaoliu.vod.utils.AliyunVodSDKUtils;
import com.xiaoliu.vod.utils.ConstantVodUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(description="阿里云视频点播微服务")
@RestController
//@CrossOrigin
@RequestMapping("/eduvod/vod")
public class VodController {

    @Autowired
    private VodService vodService;

    //上传视频到阿里云
    @ApiOperation(value = "上传视频")
    @PostMapping("/uploadAlyVideo")
    public R uploadVideo(MultipartFile file){
        String videoId=vodService.uplaodVideo(file);
        return R.ok().data("videoId",videoId);
    }

    //根据视频ID删除视频
    @ApiOperation(value = "删除视频")
    @DeleteMapping("/deleteVideo/{videoId}")
    public R deleteVideo(@PathVariable String videoId){
        vodService.removeVideo(videoId);
        return R.ok();
    }

    //删除多个视频的方法
    //参数是多个视频ID
    @DeleteMapping("/delete-batch")
    public R deleteBatch(@RequestParam("/videoList") List<String> videoList){
        vodService.removeMoreAlyVideo(videoList);
        return R.ok();
    }

    //获得上传视频的凭证，播放视频有两种方式：第一就是直接根据视频地址进行播放，但容易被人攻击服务器，
    //第二种就是根据视频凭证播放
    @GetMapping("getPlayAuth/{videoId}")
    public R getVideoPlayAuth(@PathVariable("videoId") String videoId)  {
        try{
        //获取阿里云存储相关常量
        String accessKeyId = ConstantVodUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantVodUtils.ACCESS_KEY_SECRET;

        //初始化
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(accessKeyId, accessKeySecret);

        //请求
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);
        request.setAuthInfoTimeout(200L);

        //响应
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);

        //得到播放凭证
        String playAuth = response.getPlayAuth();

        //返回结果
        return R.ok().message("获取凭证成功").data("playAuth", playAuth);
        }catch(Exception e){
            throw new XiaoliuException(20001,"获取凭证失败");
        }

    }
}
