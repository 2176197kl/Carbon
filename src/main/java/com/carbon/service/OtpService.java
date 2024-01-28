package com.carbon.service;

import com.carbon.service.SMSService;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    @Autowired
    private SMSService smsService;

    // 存储验证码的缓存，

    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    public Map<String, Object> SmsSendOTP(String phoneNumber) {
        Map<String, Object> map = new HashMap<>();
        // 生成随机的验证码
        String otp = generateOTP();

        // 发送短信
        smsService.sendSMS(phoneNumber, otp);

        // 存储验证码到缓存
        redisTemplate.opsForValue().set(phoneNumber, otp, 60, TimeUnit.SECONDS);

        map.put("success","已发送验证码");
        return map;
    }

    public Map<String, Object> SmsVerifyOTP(String phoneNumber, String otp) {
        Map<String, Object> map = new HashMap<>();
        // 从缓存中获取之前存储的验证码
        String smsVerifyCodeInRedis = (String) redisTemplate.opsForValue().get(phoneNumber);
        if (smsVerifyCodeInRedis != null && smsVerifyCodeInRedis.equals(otp)) {
            // 验证通过，可以进行登录等操作
            redisTemplate.delete(phoneNumber); // 验证通过后移除验证码
            map.put("success","验证成功");
            return map;
        } else {
            map.put("fail","验证失败");
            return map;
        }
    }

    private String generateOTP() {
        // 生成6位数字的随机验证码
        return String.format("%06d", new Random().nextInt(999999));
    }
}
