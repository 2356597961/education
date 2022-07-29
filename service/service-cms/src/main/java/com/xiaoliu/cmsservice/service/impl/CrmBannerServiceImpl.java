package com.xiaoliu.cmsservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoliu.cmsservice.entity.CrmBanner;
import com.xiaoliu.cmsservice.mapper.CrmBannerMapper;
import com.xiaoliu.cmsservice.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-11-05
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Override
    @Cacheable(key = "'selectIndexList'",value = "banner")
    public List<CrmBanner> getBannerList() {
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        //根据ID查询,显示排列后的两条记录
        wrapper.orderByDesc("id");
        //拼接SQL语句
        wrapper.last("limit 2");
        List<CrmBanner> list = baseMapper.selectList(wrapper);
        return list;
    }
}
