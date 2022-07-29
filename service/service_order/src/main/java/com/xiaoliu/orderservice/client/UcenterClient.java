package com.xiaoliu.orderservice.client;


import com.xiaoliu.commonutils.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name="service-ucenter",fallback = UcenterImpl.class)
public interface UcenterClient {

    //根据会员id查询用户信息，这里的用户id是根据token解析出来的，然后在传入这个方法的id中
    @PostMapping("/ucenterservice/member/getInfoUc/{memberId}")
    public Member getInfo(@PathVariable String memberId);

}
