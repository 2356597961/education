package com.xiaoliu.ucenterservice.service;

import com.xiaoliu.ucenterservice.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoliu.ucenterservice.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-11-11
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //登录的方法
    String login(UcenterMember member);

    //注册
    void register(RegisterVo registerVo);

    Integer countRegisterByDay(String day);
}
