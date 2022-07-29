package com.xiaoliu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.xiaoliu.msmservice.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {

    @Override
    public Boolean send(Map<String, Object> param, String phone) {
        if (StringUtils.isEmpty(phone)) return false;

        //阿里云id和密钥
        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAI5tB5rVXuwsMeEqCACwFx",
                        "HQ7RDk1fV7U59LOER7y6miVQJpydPd");
        IAcsClient client = new DefaultAcsClient(profile);

        //设置相关固定参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        //提交方式
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置发送相关的参数,键都是固定的
        request.putQueryParameter("PhoneNumbers", phone);//手机号
        request.putQueryParameter("SignName", "xiaoliu");//阿里云设置的签名
        request.putQueryParameter("TemplateCode", "SMS_227746326");//模板code
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param)); //验证码，阿里云指定要使用JOSN格式传递

        try {
            //最终发送
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
