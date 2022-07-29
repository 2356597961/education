package com.xiaoliu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.xiaoliu.oss.service.OssService;
import com.xiaoliu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传文件
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName=ConstantPropertiesUtils.BUCKET_NAME;
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //判断oss实列是否存在，如果不存在则创建，如果存在则获取
            if (!ossClient.doesBucketExist(bucketName)) {
                //创建bucket
                ossClient.createBucket(bucketName);
                //设置oss实列的访问权限：公共读
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }
            //获取真实路径名
            String originalFilename = file.getOriginalFilename();
            //创建日期路径，一个工具类
            String filePath=new DateTime().toString("yyyy/MM/dd");
            //文件名：uuid.扩展名,把随机生成的“-”换成“”
            String fileName = UUID.randomUUID().toString().replace("-","");
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newName=fileName+ fileType;
            String fileUrl=filePath+"/"+newName;
            // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            InputStream inputStream = file.getInputStream();
            // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName,fileUrl, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //需要把上传到阿里云oss路径手动拼接出来
            String url="https://"+bucketName+"."+endpoint+"/"+fileUrl;
            //https://xiaoliu-edu.oss-cn-guangzhou.aliyuncs.com/2021/10/15/03a36073-a140-4942-9b9b-712cecb144901.jpg
            return url;
        }catch (Exception e){
          e.printStackTrace();
          return null;
        }
    }
}
