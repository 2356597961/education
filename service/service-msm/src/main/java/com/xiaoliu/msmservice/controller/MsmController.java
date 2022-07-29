package com.xiaoliu.msmservice.controller;

import com.xiaoliu.commonutils.R;
import com.xiaoliu.msmservice.service.MsmService;
import com.xiaoliu.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
//短信服务
@RestController
//@CrossOrigin
@RequestMapping("/msmservice/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate redisTemplate;

    //发送短信方法
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone){
        //如果能从缓存中取到，就直接返回
        String rediscode = (String) redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(rediscode)){
            return R.ok().message("短信已发送");
        }
        //取不到验证码就，阿里云发送，在缓存起来
        //生成随机数，传递给阿里云进行发送
        String code= RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code",code);
        //service调用发送短信服务
        Boolean isSend=msmService.send(param,phone);
        if (isSend){
            //设置有效时间5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok().message("短信发送成功");
        }
        return R.error().message("短信发送失败");
    }
}
