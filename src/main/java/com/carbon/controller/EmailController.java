package com.carbon.controller;

import com.carbon.service.EmailService;
import com.carbon.service.UserService;
import com.carbon.util.CarbonConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/email")
public class EmailController implements CarbonConstant {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private UserService userService;


    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 通过邮箱重置密码
     */
    @PostMapping("/resetPwd")
    @ResponseBody
    public Map<String, Object> resetPwd(@RequestParam("account") String account,
                        @RequestParam("password") String password,
                        @RequestParam("emailVerifyCode") String emailVerifyCode) {
        Map<String, Object> map = new HashMap<>(4);

        // 检查邮件验证码
        String emailVerifyCodeCheckRst = checkRedisResetPwdEmailCode(account, emailVerifyCode);
        if (StringUtils.isNotBlank(emailVerifyCodeCheckRst)) {
            map.put("status", "1");
            map.put("errMsg", emailVerifyCodeCheckRst);
            return map;
        }
        // 执行重置密码操作
        Map<String, Object> stringObjectMap = EmailService.doResetPwd(account, password);
        String accountMsg = (String) stringObjectMap.get("errMsg");
        if (StringUtils.isBlank(accountMsg)) {
            map.put("status", "0");
            map.put("msg", "重置密码成功!");
            map.put("target", "/login");
        }
        return map;
    }

    /**
     * 发送邮件验证码(用于重置密码)
     *
     * @param account 用户输入的需要找回的账号
     */
    @PostMapping("/sendEmailCodeForResetPwd")
    @ResponseBody
    public Map<String, Object> sendEmailCodeForResetPwd(Model model, @RequestParam("account") String account) {
        Map<String, Object> map = new HashMap<>(3);

        Map<String, Object> stringObjectMap = EmailService.doSendEmailCodeForResetPwd(account);
        String accountMsg = (String) stringObjectMap.get("errMsg");
        if (StringUtils.isBlank(accountMsg)) {
            map.put("status", "0");
            map.put("msg", "已经往您的邮箱发送了一封验证码邮件, 请查收!");
        }
        return map;
    }

    /**
     * 检查 邮件 验证码
     *
     * @param account 用户名
     * @param checkCode 用户输入的邮箱验证码
     * @return 验证成功 返回"", 失败则返回原因
     */
    private String checkRedisResetPwdEmailCode(String account, String checkCode) {
        if (StringUtils.isBlank(checkCode)) {
            return "未发现输入的邮件验证码";
        }
        final String redisKey = "EmailCodeForResetPwd:" + account;
        String emailVerifyCodeInRedis = (String) redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(emailVerifyCodeInRedis)) {
            return "邮件验证码已过期";
        } else if (!emailVerifyCodeInRedis.equalsIgnoreCase(checkCode)) {
            return "邮件验证码错误";
        }
        return "";
    }


}
