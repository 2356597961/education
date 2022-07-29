package com.xiaoliu.eduservice.controller;

import com.xiaoliu.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * 登录界面
 */
@Api(tags = "登录")
@RestController
//@CrossOrigin //解决跨域问题
@RequestMapping("/eduservice/user")
public class EduLoginController {

    //login登录
    @PostMapping("/login")
    public R login(){
        return R.ok().data("token","admin");
    }

    //info信息
    @GetMapping("/info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
