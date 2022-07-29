package com.xiaoliu.orderservice.service;

import com.xiaoliu.orderservice.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-12-01
 */
public interface PayLogService extends IService<PayLog> {

    //生成二维码
    Map createNative(String orderNo);

    Map<String, String> queryPayStatus(String orderNo);

    void updateOrderStatus(Map<String, String> map);

}
