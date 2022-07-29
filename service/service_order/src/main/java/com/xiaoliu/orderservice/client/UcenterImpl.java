package com.xiaoliu.orderservice.client;

import com.xiaoliu.commonutils.Member;
import org.springframework.stereotype.Component;

//调用失败就会来到这里
@Component
public class UcenterImpl implements UcenterClient{
    @Override
    public Member getInfo(String memberId) {
        return null;
    }
}
