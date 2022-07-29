package com.xiaoliu.orderservice.controller;


import com.xiaoliu.commonutils.R;
import com.xiaoliu.orderservice.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-01
 */
//@CrossOrigin
@RestController
@RequestMapping("/orderservice/paylog")
public class PayLogController {

    @Autowired
    private PayLogService payService;
    /**
     * 生成二维码
     *
     * @return
     */
    //点去支付的时候就会调用这个接口,根据订单号生成二维码
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        //返回信息，包含二维码的地址，还有其他信息
        Map map = payService.createNative(orderNo);
        return R.ok().data(map); //没有其他键了
    }

    //在查询的同时一起来判断是否已经支付了，支付完成后写入支付记录表
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        //调用查询接口
        Map<String, String> map = payService.queryPayStatus(orderNo);
        if (map == null) {//出错
            return R.error().message("支付出错");
        }
        //map包括了支付是否成功的放回的
        if (map.get("trade_state").equals("SUCCESS")) {//如果成功
            //更改订单状态，在更新订单表的同时添加记录到支付表中
            payService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中"); //25000一直处在支付中，前端request.js拦截，20000改为了25000
    }
}

