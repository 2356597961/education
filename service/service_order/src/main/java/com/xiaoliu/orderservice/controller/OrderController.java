package com.xiaoliu.orderservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoliu.commonutils.JwtUtils;
import com.xiaoliu.commonutils.R;
import com.xiaoliu.orderservice.entity.Order;
import com.xiaoliu.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-01
 */
//@CrossOrigin
@RestController
@RequestMapping("/orderservice/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    //点击立即购买就会调用这个方法，生成订单，放回订单号给下一步查询出订单内容
    //根据课程id和用户id创建订单，返回订单id,给订单记录使用
    @PostMapping("createOrder/{courseId}")
    public R save(@PathVariable String courseId, HttpServletRequest request) {
        String orderId = orderService.saveOrder(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderId", orderId);
    }

    @GetMapping("getOrderById/{orderId}")
    public R get(@PathVariable String orderId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("item", order);
    }

    //查询是否购买了课程，在课程详情页面进行判断是否立即购买或者立即观看
    @GetMapping("isBuyCourse/{memberid}/{id}")
    public boolean isBuyCourse(@PathVariable String memberid,
                               @PathVariable String id) {
        //订单状态是1表示支付成功
        int count = orderService.count(new QueryWrapper<Order>().eq("member_id", memberid).eq("course_id", id).eq("status", 1));
        if(count>0) {
            return true;
        } else {
            return false;
        }
    }

}

