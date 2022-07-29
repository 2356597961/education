package com.xiaoliu.cmsservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoliu.cmsservice.entity.CrmBanner;
import com.xiaoliu.cmsservice.service.CrmBannerService;
import com.xiaoliu.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-11-05
 */
//后台使用
@RestController
@RequestMapping("/cmsservice/adminbanner")
//@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    //根据条件分页查询
    @PostMapping("/pageBannerByCondition/{current}/{limit}")
    public R pageBannerByCondition(@PathVariable long current, @PathVariable long limit,
                                   @RequestBody CrmBanner crmBanner){
        Page<CrmBanner> bannerPage = new Page<>(current, limit);
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        if (!crmBanner.getTitle().isEmpty()){
            wrapper.eq("title",crmBanner.getTitle());
        }
        bannerService.page(bannerPage,wrapper);
        long total = bannerPage.getTotal();
        List<CrmBanner> records = bannerPage.getRecords();
        return R.ok().data("total",total).data("items",records);
    }

    //添加banner
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        boolean save = bannerService.save(crmBanner);
        if (save){
            return R.ok().message("添加成功");
        }else {
            return R.error();
        }
    }

    //根据ID删除Banner
    @DeleteMapping("/deleteBanner/{id}")
    public R DeleteBanner(@PathVariable String id){
        boolean b = bannerService.removeById(id);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //修改banner
    @PutMapping("/updateBanner")
    public R updateBanner(@RequestBody CrmBanner banner){
        boolean b = bannerService.updateById(banner);
        if (b){
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据ID获得banner
    @GetMapping("/getBannerById/{id}")
    public R getBannerById(@PathVariable String id){
        CrmBanner  banner= bannerService.getById(id);
        if (banner!=null){
            return R.ok().data("banner",banner);
        }else {
            return R.error();
        }
    }
}

