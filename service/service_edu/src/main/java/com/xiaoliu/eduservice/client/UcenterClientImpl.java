package com.xiaoliu.eduservice.client;

import com.xiaoliu.commonutils.Member;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientImpl implements UcenterClient {
    //实现类实现接口，熔断器判断了出错，就执行该方法
    @Override
    public Member getUcenterComment(String memberId) {
        return null;
    }
}
