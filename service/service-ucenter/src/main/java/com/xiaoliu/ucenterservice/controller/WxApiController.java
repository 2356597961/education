package com.xiaoliu.ucenterservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.xiaoliu.commonutils.JwtUtils;
import com.xiaoliu.servicebase.exceptionhandler.XiaoliuException;
import com.xiaoliu.ucenterservice.entity.UcenterMember;
import com.xiaoliu.ucenterservice.service.UcenterMemberService;
import com.xiaoliu.ucenterservice.utils.ConstantWeixinUtils;
import com.xiaoliu.ucenterservice.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller//注意这里没有配置 @RestController因为这个是返回数据的
@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

@Autowired
private UcenterMemberService memberService;

    /**
     * @param code
     * @param state
     * @return
     */
    @GetMapping("callback")
    public String callback(String code, String state){

        //得到授权临时票据code
        System.out.println(code);
        System.out.println(state);

        //从redis中将state获取出来，和当前传入的state作比较
        //如果一致则放行，如果不一致则抛出异常：非法访问

        //向认证服务器发送请求换取access_token,拼接三个参数：id,密钥，code值
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        //请求这个拼接好的地址，得到返回的两个值accsess_token和openid
        //使用httpclient发送请求，得到返回的结果，这个httpclient是比较古老的技术，但是这个是不用浏览器也能发送请求的

        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                ConstantWeixinUtils.WX_OPEN_APP_ID,
                ConstantWeixinUtils.WX_OPEN_APP_SECRET,
                code);

        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessToken=============" + result);
        } catch (Exception e) {
            throw new XiaoliuException(20001, "获取access_token失败");
        }

        //解析json字符串，转换为map集合，gson工具使用
        Gson gson = new Gson();
        HashMap map = gson.fromJson(result, HashMap.class);
        String accessToken = (String)map.get("access_token");
        String openid = (String)map.get("openid");

        //查询数据库当前用用户是否曾经使用过微信登录
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = memberService.getOne(wrapper);
        //有相应的用户就直接跳转相应的地址
        if(member == null){
            System.out.println("新用户注册");

            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
            //用户信息
            String resultUserInfo = null;
            try {
                //获得用户信息
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultUserInfo==========" + resultUserInfo);
            } catch (Exception e) {
                throw new XiaoliuException(20001, "获取用户信息失败");
            }

            //解析json，解析用户信息字符串
            HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
            //还有其他的具体看需要，取出相应的
            //获得昵称
            String nickname = (String)mapUserInfo.get("nickname");
            //获得头像地址
            String headimgurl = (String)mapUserInfo.get("headimgurl");

            //向数据库中插入一条记录
            member = new UcenterMember();
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            memberService.save(member);
        }

        //使用jwt根据member对象生成token字符串
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        //最后会返回首页，通过路径传递toke字符串,这个地址是前端首页地址3000端口
        return "redirect:http://localhost:3000?token="+jwtToken;
    }


    //生成二维码，可以有一个点击就生成二维码的地方，比如点击图标等，还可以直接在本页面就行了，无需跳转到其他页面
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {

        //怎样把二维码嵌入到一个页面中，完善的地方？

        // 微信开放平台授权baseUrl，固定地址后面拼接参数
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //也可以这样写请求地址
        //"https://open.weixin.qq.com/connect/qrconnect?"appid=" + ConstantWeixinUtils.WX_OPEN_REDIRECT_URL+""....
        // 要求对redirect_url进行编码，回调地址
        String redirectUrl = ConstantWeixinUtils.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new XiaoliuException(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "xiaoliu";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        System.out.println("state = " + state);

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantWeixinUtils.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        //重定向到微信的请求地址中去,
        //扫码后跳转到http://localhost:8160/api/ucenter/wx/callback?code=031UMSll2wvy884ONynl2HZhKh4UMSlD&state=xiaoliu
        return "redirect:" + qrcodeUrl;
    }
}
