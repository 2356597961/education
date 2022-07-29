package com.xiaoliu.cmsservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoliu.cmsservice.entity.CrmBanner;
import com.xiaoliu.cmsservice.service.CrmBannerService;
import com.xiaoliu.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-11-05
 */
//前台使用
@RestController
@RequestMapping("/cmsservice/frontbanner")
//@CrossOrigin
@Api(tags = "前台轮播图")
public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;

    //查出所有的轮播图页面
    @GetMapping("/getAllBannner")
    public R getBanner(){
        List<CrmBanner> list = bannerService.getBannerList();
        if (list!=null){
            return R.ok().data("list",list);
        }else {
            return R.error();
        }
    }
}

