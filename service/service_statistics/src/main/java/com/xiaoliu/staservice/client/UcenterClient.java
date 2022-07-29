package com.xiaoliu.staservice.client;

import com.xiaoliu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {

    @GetMapping(value = "/ucenterservice/member/countregister/{day}")
    public R registerCount(@PathVariable("day") String day);
}
