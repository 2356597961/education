package com.xiaoliu.servicebase.exceptionhandler;


import com.xiaoliu.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j  //异常信息输出到文件
public class GlobalExceptionHandler {

    //指定出现什么异常会执行该方法
    @ExceptionHandler(Exception.class)
    @ResponseBody //为了能返回数据
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理..");
    }

    //特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了ArithemticException异常处理..");
    }

    //自定义异常
    @ExceptionHandler(XiaoliuException.class)
    @ResponseBody
    public R error(XiaoliuException e){
        log.error(e.getMessage()); //会写到error的文件中
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
