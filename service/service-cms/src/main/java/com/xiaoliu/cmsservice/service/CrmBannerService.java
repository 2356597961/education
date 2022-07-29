package com.xiaoliu.cmsservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoliu.cmsservice.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-11-05
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> getBannerList();
}
