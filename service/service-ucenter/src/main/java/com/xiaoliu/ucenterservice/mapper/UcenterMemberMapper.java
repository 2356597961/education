package com.xiaoliu.ucenterservice.mapper;

import com.xiaoliu.ucenterservice.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-11-11
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    Integer selectRegisterCount(String day);
}
