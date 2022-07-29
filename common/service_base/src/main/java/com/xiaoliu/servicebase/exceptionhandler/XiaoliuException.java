package com.xiaoliu.servicebase.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //生成get set方法
@AllArgsConstructor //生成有参数的构造方法
@NoArgsConstructor //生成无参数的构造方法
public class XiaoliuException extends RuntimeException{

    private Integer code;//状态码

    private String msg;//异常信息


}
