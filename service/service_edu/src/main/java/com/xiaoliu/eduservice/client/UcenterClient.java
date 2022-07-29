package com.xiaoliu.eduservice.client;

import com.xiaoliu.commonutils.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name="service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {

    //根据用户id获取用户信息
    @PostMapping("/ucenterservice/member/getInfoUc/{memberId}")
    public Member getUcenterComment(@PathVariable("memberId") String memberId);
}
