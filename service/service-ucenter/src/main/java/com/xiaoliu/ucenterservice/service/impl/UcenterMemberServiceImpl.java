package com.xiaoliu.ucenterservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoliu.commonutils.JwtUtils;
import com.xiaoliu.commonutils.MD5;
import com.xiaoliu.servicebase.exceptionhandler.XiaoliuException;
import com.xiaoliu.ucenterservice.entity.UcenterMember;
import com.xiaoliu.ucenterservice.entity.vo.RegisterVo;
import com.xiaoliu.ucenterservice.mapper.UcenterMemberMapper;
import com.xiaoliu.ucenterservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-11-11
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate redisTemplate;
    //登录的方法
    @Override
    public String login(UcenterMember member) {
        String password = member.getPassword();
        String mobile = member.getMobile();
        if (StringUtils.isEmpty(password) ||StringUtils.isEmpty(mobile)){
            throw new XiaoliuException(20001,"登录失败");
        }
        //判断手机号是否为空
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        if (mobileMember==null){ //没有该手机号
            throw new XiaoliuException(20001,"登录失败");
        }

        //如果不为空判断密码是否正确
        //存到数据库的密码是加密的,所以从加密密码，在和数据库中比较
        //加密方式MD5
        String mobilepassword = mobileMember.getPassword();
        if (!MD5.encrypt(password).equals(mobilepassword)){
            throw new XiaoliuException(20001,"登录失败");
        }

        //判断用户是否被禁用
        if (mobileMember.getIsDisabled()){
            throw new XiaoliuException(20001,"登录失败");
        }

        //登录成功
        //根据JWT工具生成token字符串返回,将token值放在请求头
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return jwtToken;
    }

    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode(); //验证码
        String mobile = registerVo.getMobile(); //手机号
        String nickname = registerVo.getNickname(); //昵称
        String password = registerVo.getPassword(); //密码

        //非空判断
        if (StringUtils.isEmpty(mobile) ||StringUtils.isEmpty(code) ||StringUtils.isEmpty(nickname)
                ||StringUtils.isEmpty(password)){
            throw new XiaoliuException(20001,"注册失败");
        }

        //判断验证码是否和redis中验证码一致
        String rediscode = (String)redisTemplate.opsForValue().get(mobile);
        if (!code.equals(rediscode)){
            throw new XiaoliuException(20001,"登录失败");
        }

        //判断手机号是否已经存在，表理存在相同的手机号，不进行添加，否则添加
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count>0){
            throw new XiaoliuException(20001,"登录失败");
        }

        //用户信息添加到数据库中
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        ucenterMember.setNickname(nickname);
        ucenterMember.setPassword(MD5.encrypt(password));
        ucenterMember.setIsDisabled(false);//用户不被禁用
        ucenterMember.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        baseMapper.insert(ucenterMember);
    }

    @Override
    public Integer countRegisterByDay(String day) {
        //调用自己写的sql语句
        return baseMapper.selectRegisterCount(day);
    }
}
