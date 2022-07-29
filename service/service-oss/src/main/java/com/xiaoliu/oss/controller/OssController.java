package com.xiaoliu.oss.controller;

import com.xiaoliu.commonutils.R;
import com.xiaoliu.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
//@CrossOrigin
@EnableDiscoveryClient //nacos注册
@Api(description = "阿里云文件管理")
public class OssController {

    @Autowired
    private OssService ossService;
    //上传头像的方法
    @ApiOperation(value = "上传文件")
    @PostMapping("/upload")
    public R uploadOssFile(MultipartFile file){
        //获取上传文件
        //返回上传到oss的路径
        String url=ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }
}
