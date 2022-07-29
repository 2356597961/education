package com.xiaoliu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    String uplaodVideo(MultipartFile file);

    void removeVideo(String videoId);

    void removeMoreAlyVideo(List videoList);
}
