package com.xiaoliu.orderservice.service;

import com.xiaoliu.orderservice.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-12-01
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String memberIdByJwtToken);
}
