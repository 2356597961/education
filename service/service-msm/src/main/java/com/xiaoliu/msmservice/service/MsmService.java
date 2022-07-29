package com.xiaoliu.msmservice.service;

import java.util.Map;

public interface MsmService {

    //发送短信
    Boolean send(Map<String, Object> param, String phone);
}
