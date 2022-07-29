package com.xiaoliu.vod.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;

public class AliyunVodSDKUtils {
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws Exception {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
