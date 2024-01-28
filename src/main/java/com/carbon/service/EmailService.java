package com.carbon.service;

import com.carbon.dao.UserMapper;
import com.carbon.entity.User;
import com.carbon.util.CarbonUtil;
import com.carbon.util.MailClient;
import com.carbon.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Service
public class EmailService {
    @Autowired
    private static UserMapper userMapper;


    // 网站域名
    @Value("${carbon.path.domain}")
    private static String domain;

    // 项目名(访问路径)
    @Value("${server.servlet.context-path}")
    private static String contextPath;
    @Qualifier("redisTemplate")
    @Autowired
    private static RedisTemplate redisTemplate;
    @Qualifier("mailClient")
    @Autowired
    private static MailClient mailClient;
    @Autowired
    private static TemplateEngine templateEngine;
    /**
     * 发送邮箱验证码
     * @param account 账户名, 目前是用户名
     *
     * @return Map<String, Object> 返回错误提示消息，如果返回的 map 为空，则说明发送验证码成功
     */
    public static Map<String, Object> doSendEmailCodeForResetPwd(String account) {
        Map<String, Object> map = new HashMap<>(2);
        User user = userMapper.selectByName(account);
        if (user == null) {
            map.put("errMsg", "未发现账号");
            return map;
        }
        final String email = user.getEmail();
        if (StringUtils.isBlank(email)) {
            map.put("errMsg", "该账号未绑定邮箱");
            return map;
        }

        // 生成6位验证码
        String randomCode = CarbonUtil.getRandomCode(6);
        // 给注册用户发送激活邮件
        Context context = new Context();
        context.setVariable("email", "您的验证码是 " + randomCode);
        // http://localhost:8080/carbon
        String url = domain + contextPath + "/activation/" + user.getId();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(email,"重置密码", content);

        final String redisKey = "EmailCodeForResetPwd:" + account;

        redisTemplate.opsForValue().set(redisKey, randomCode, 60, TimeUnit.SECONDS);
        return map;
    }

    /**
     * 发送邮箱验证码
     * @param account 账户名, 目前是用户名
     *
     * @return Map<String, Object> 返回错误提示消息，如果返回的 map 为空，则说明发送验证码成功
     */
    public static Map<String, Object> doResetPwd(String account, String password) {
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

    /**
     * 用户信息变更时清除对应缓存数据
     * @param userId
     */
    private static void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }
}
