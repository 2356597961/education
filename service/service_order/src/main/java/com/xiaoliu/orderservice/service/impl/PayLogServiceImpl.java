package com.xiaoliu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.xiaoliu.orderservice.entity.Order;
import com.xiaoliu.orderservice.entity.PayLog;
import com.xiaoliu.orderservice.mapper.PayLogMapper;
import com.xiaoliu.orderservice.service.OrderService;
import com.xiaoliu.orderservice.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoliu.orderservice.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-01
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;
    //生成二维码等其他信息
    @Override
    public Map createNative(String orderNo) {
        try {
            //根据订单id获取订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(wrapper);

            Map m = new HashMap();
            //1、设置支付参数用来设置二维码参数
            m.put("appid", "wx74862e0dfcf69954"); //关联的公众号appid
            m.put("mch_id", "1558950191");  //商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr());  //微信支付依赖包的工具类，生成随机字符一串，可以自己写
            m.put("body", order.getCourseTitle());  //课程标题，可以是其他的
            m.put("out_trade_no", orderNo); //订单号唯一的
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + ""); //将元转换为分
            m.put("spbill_create_ip", "127.0.0.1"); //这里是使用的域名
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n"); //支付后回掉地址
            m.put("trade_type", "NATIVE"); //支付方式，微信扫描支付

            //2、HTTPClient来根据URL访问第三方接口并且传递参数，固定的地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数
            //加密
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post(); //发送post请求
            //3、返回第三方的数据，格式是xml格式
            String xml = client.getContent();
            //转换为map类型，这里只有二维码地址和状态码等
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            //4、封装返回结果集
            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    //查询订单支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            //6、转成Map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //更新订单支付状态和写入支付记录
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //获取订单id,微信支付返回来的一样的
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

        if(order.getStatus().intValue() == 1) return;
        order.setStatus(1);
        orderService.updateById(order);

        //记录支付日志
        PayLog payLog=new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());  //支付时间
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id")); //交易流水号
        //fastJson的使用，还有gson创建对象，然后使用
        payLog.setAttr(JSONObject.toJSONString(map)); //其他属性等
        baseMapper.insert(payLog);//插入到支付日志表
    }
}
