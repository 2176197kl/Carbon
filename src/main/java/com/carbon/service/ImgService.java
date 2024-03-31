package com.carbon.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Service
public class ImgService {
    @Value("${aliyun.key.access}")
    private String accessKeyId;

    @Value("${aliyun.key.secret}")
    private String accessKeySecret;

    @Value("${aliyun.bucket.header.name}")
    private String bucketName;


    @Value("${aliyun.bucket.header.endpoint}")
    private String endPoint;

    public String upload(MultipartFile file) {



        //创建OSS对象
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

        //获取原生文件名
        String originalFilename = file.getOriginalFilename();
        //JDK8的日期格式
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        //拼装OSS上存储的路径
        String folder = dft.format(time);
        String fileName = generateUUID();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        //在OSS上bucket下的文件名
        //String uploadFileName = "user/" + folder + "/" + fileName + extension;
        String uploadFileName = "user/" + folder + "/" + fileName + extension;
        try {
            PutObjectResult result = ossClient.putObject(bucketName, uploadFileName, file.getInputStream());
            //拼装返回路径
            if (result != null) {
                return "https://"+bucketName+"."+endPoint+"/"+uploadFileName;
            }
        } catch (IOException e) {
            log.print("文件上传失败:{}"+e.getMessage());
        } finally {
            //OSS关闭服务，不然会造成OOM
            ossClient.shutdown();
        }
        return null;
    }

    /**
     * 获取随机字符串
     * @return
     */
    private String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

}
