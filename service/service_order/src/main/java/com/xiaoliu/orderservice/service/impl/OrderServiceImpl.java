package com.xiaoliu.orderservice.service.impl;

import com.xiaoliu.commonutils.Course;
import com.xiaoliu.commonutils.Member;
import com.xiaoliu.orderservice.client.EduClient;
import com.xiaoliu.orderservice.client.UcenterClient;
import com.xiaoliu.orderservice.entity.Order;
import com.xiaoliu.orderservice.mapper.OrderMapper;
import com.xiaoliu.orderservice.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoliu.orderservice.utils.OrderNoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-01
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private EduClient eduClient;
    //生成订单
    @Override
    public String saveOrder(String courseId, String memberId) {

        //远程调用根据课程ID获取课程信息
        Course courseInfoDto = eduClient.getCourseInfoDto(courseId);

        //远程调用根据用户Id获取用户信息
        Member info = ucenterClient.getInfo(memberId);

        Order order = new Order();

        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoDto.getTitle());
        order.setCourseCover(courseInfoDto.getCover());
        order.setTeacherName("test");
        order.setTotalFee(courseInfoDto.getPrice());
        order.setMemberId(memberId);
        order.setMobile(info.getMobile());
        order.setNickname(info.getNickname());
        order.setStatus(0);   //支付状态
        order.setPayType(1);  //支付类型
        baseMapper.insert(order);

        return order.getOrderNo();
    }
}
