package com.xiaoliu.eduservice.client;

import org.springframework.stereotype.Component;

@Component
public class OrderClientImpl implements OrderClient {
    @Override
    public boolean isBuyCourse(String memberid, String id) {
        System.out.println("false");
        return false;
    }
}
