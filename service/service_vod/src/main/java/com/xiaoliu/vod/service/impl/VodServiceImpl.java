package com.xiaoliu.vod.service.impl;



import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.xiaoliu.commonutils.R;
import com.xiaoliu.servicebase.exceptionhandler.XiaoliuException;
import com.xiaoliu.vod.service.VodService;
import com.xiaoliu.vod.utils.AliyunVodSDKUtils;
import com.xiaoliu.vod.utils.ConstantVodUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {
    //上传视频
    @Override
    public String uplaodVideo(MultipartFile file) {
        //title表示为上到阿里云的视频名称
        //filename表示为上传本地视频名称
        //inputStream表示为输入流
        try {
            String fileName = file.getOriginalFilename();
            //取文件名前的名称
            String title = fileName.substring(0,fileName.indexOf("."));
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String videoId = response.getVideoId();
            if (response.isSuccess()) {
                System.out.print("VideoId=" + videoId + "\n");
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
                // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                System.out.print("VideoId=" + videoId + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
            return videoId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new XiaoliuException(20001, "上传失败");
        }
    }

    //删除阿里云视频
    @Override
    public void removeVideo(String videoId) {
        try{
            //初始化对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.ACCESS_KEY_SECRET);

            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();

            //向request设置视频ID
            request.setVideoIds(videoId);

            //调用初始化对象的方法删除视频
            DeleteVideoResponse response = client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
            throw new XiaoliuException(20001, "视频删除失败");
        }
    }

    @Override
    public void removeMoreAlyVideo(List videoList) {
        try{
            //初始化对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.ACCESS_KEY_SECRET);

            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();

            //拼接ID实现,是这个包的StringUilts
            String videoIds = StringUtils.join(videoList.toArray(), ",");
            //向request设置视频ID,这里的id要这样拼接，id,id,id
            request.setVideoIds(videoIds);

            if (!videoIds.isEmpty()){
            //调用初始化对象的方法删除视频
            DeleteVideoResponse response = client.getAcsResponse(request);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new XiaoliuException(20001, "视频删除失败");
        }
    }
}
