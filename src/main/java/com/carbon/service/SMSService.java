package com.carbon.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.carbon.dao.UserMapper;
import com.carbon.entity.User;
import com.carbon.util.CarbonUtil;
import com.carbon.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;





@Service
public class SMSService {
    @Value("${aliyun.key.access}")
    private String accessKeyId;

    @Value("${aliyun.key.secret}")
    private String accessKeySecret;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ReactiveRedisOperations<Object, Object> redisTemplate;

    public Boolean sendSMS(String phoneNumber, String message) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        // 构建请求：
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        // 自定义参数：
        request.putQueryParameter("PhoneNumbers", phoneNumber);// 手机号
        request.putQueryParameter("SignName", "Carbon");// 短信签名
        request.putQueryParameter("TemplateCode", "SMS_464980305");// 短信模版CODE
        // 构建短信验证码 必须和模板保持一致
        Map<String,Object> map = new HashMap<>();
        map.put("code",message);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 用户注册
     * @param user
     * @return Map<String, Object> 返回错误提示消息，如果返回的 map 为空，则说明注册成功
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getAccount())) {
            map.put("accountMsg", "账号不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getPhone())) {
            map.put("phoneMsg", "手机号不能为空");
            return map;
        }

        // 验证账号是否已存在
        User u = userMapper.selectByName(user.getAccount());
        if (u != null) {
            map.put("accountMsg", "该账号已存在");
            return map;
        }



        // 注册用户
        user.setType(0); // 默认普通用户
        user.setUsername("新用户");
        // 随机头像（用户登录后可以自行修改）
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date()); // 注册时间
        userMapper.insertUser(user);


        return map;
    }
    /**
     * 用户登录（为用户创建凭证）
     * @param account
     * @param password
     * @param expiredSeconds 多少秒后凭证过期
     * @return Map<String, Object> 返回错误提示消息以及 ticket(凭证)
     */
    public Map<String, Object> login(String account, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(account)) {
            map.put("accountMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(account);
        if (user == null) {
            map.put("accountMsg", "该账号不存在");
            return map;
        }


        // 验证密码
        password = CarbonUtil.md5(password);
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码错误");
            return map;
        }

        // 用户名和密码均正确，为该用户生成登录凭证
        com.carbon.entity.LoginTicket loginTicket = new com.carbon.entity.LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CarbonUtil.generateUUID()); // 随机凭证
        loginTicket.setStatus(0); // 设置凭证状态为有效（当用户登出的时候，设置凭证状态为无效）
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000)); // 设置凭证到期时间

        // 将登录凭证存入 redis
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    //重置密码
    public  Map<String, Object> doResetPwd(String account, String password) {
        Map<String, Object> map = new HashMap<>(2);
        if (StringUtils.isBlank(password)) {
            map.put("errMsg", "密码不能为空");
            return map;
        }
        User user = userMapper.selectByName(account);
        if (user == null) {
            map.put("errMsg", "未发现账号");
            return map;
        }
        final String passwordEncode = CarbonUtil.md5(password);
        int i = userMapper.updatePassword(user.getId(), passwordEncode);
        if (i <= 0) {
            map.put("errMsg", "修改数据库密码错误");
        } else {
            clearCache(user.getId());
        }
        return map;
    }

    //更换手机号
    public Map<String,Object> updatePhone(String account,String newPhoneNumber){
        Map<String, Object> map = new HashMap<>(2);
        if (StringUtils.isBlank(newPhoneNumber)) {
            map.put("errMsg", "新手机号不能为空");
            return map;
        }
        User user = userMapper.selectByName(account);
        if (user == null) {
            map.put("errMsg", "未发现账号");
            return map;
        }

        int i = userMapper.updatePhone(user.getId(), newPhoneNumber);
        if (i <= 0) {
            map.put("errMsg", "修改数据库密码错误");
        } else {
            clearCache(user.getId());
        }
        return map;
    }

    /**
     * 用户信息变更时清除对应缓存数据
     * @param userId
     */
    private  void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }
}
