package com.xiaoliu.ucenterservice.controller;


import com.xiaoliu.commonutils.Member;
import com.xiaoliu.commonutils.JwtUtils;
import com.xiaoliu.commonutils.R;
import com.xiaoliu.servicebase.exceptionhandler.XiaoliuException;
import com.xiaoliu.ucenterservice.entity.UcenterMember;
import com.xiaoliu.ucenterservice.entity.vo.RegisterVo;
import com.xiaoliu.ucenterservice.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-11-11
 */
@CrossOrigin
@RestController
@RequestMapping("/ucenterservice/member")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //登录
    @PostMapping("/login")
    public R loginUser(@RequestBody UcenterMember member){
        //调用service方法实现登录
        //返回token值，使用jwt生成
        String token=memberService.login(member);
        return R.ok().data("token",token);
    }

    //注册
    @PostMapping("/register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){

        try {
        //调用JWT工具类的方法，根据request对象获取头信息，返回用户ID
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户ID获取用户信息
        UcenterMember member = memberService.getById(memberId);
        System.out.println(member);
        return R.ok().data("userInfo",member);
        }catch (Exception e){
            e.printStackTrace();
            throw new XiaoliuException(20001,"error");
        }
    }

    //其他的·地方使用,订单创建等使用 根据token字符串获取用户信息
    @PostMapping("getInfoUc/{memberId}")
    public Member getInfo(@PathVariable String memberId) {
        //根据用户id获取用户信息
        UcenterMember ucenterMember = memberService.getById(memberId);
        Member member = new Member();
        BeanUtils.copyProperties(ucenterMember, member);
        return member;
    }

    //统计某一天的注册人数
    @GetMapping(value = "countregister/{day}")
    public R registerCount(
            @PathVariable String day){
        Integer count = memberService.countRegisterByDay(day);
        return R.ok().data("countRegister", count);
    }
}

