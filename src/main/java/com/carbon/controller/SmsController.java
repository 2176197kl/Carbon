package com.carbon.controller;

import com.carbon.entity.User;
import com.carbon.service.EmailService;
import com.carbon.service.SMSService;
import com.carbon.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.carbon.util.CarbonConstant.DEFAULT_EXPIRED_SECONDS;
import static com.carbon.util.CarbonConstant.REMEMBER_EXPIRED_SECONDS;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private com.carbon.service.OtpService otpService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private SMSService smsService;

    @PostMapping("/send-otp")
    public Map<String, Object> sendOTP(@RequestBody User user) {
        return otpService.SmsSendOTP(user.getPhone());
    }
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @PostMapping("/login")
    public User login(@RequestBody User user,
                            @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
                            Model model, HttpServletResponse response)
    {
        // 凭证过期时间（是否记住我）
        int expiredSeconds = rememberMe ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        //若未注册，立即注册，账号与密码和手机同号
        Map<String, Object> map1 = smsService.register(user);
        Map<String,Object>map= otpService.SmsVerifyOTP(user.getPhone(), user.getOtp());



        if (map.containsKey("success")) {
            // 账号和密码均正确，则服务端会生成 ticket，浏览器通过 cookie 存储 ticket
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath); // cookie 有效范围
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return user;
        }
        else {
            model.addAttribute("fail", map.get("fail"));
            return null;
        }
    }
/*
 No such property: code for class: Script1 * @return java.lang.String
 * @description 手机快捷注册，账号默认为手机号
 * @author kl
 * @date 2024/1/28 15:48
 */
    @PostMapping("/register")
    public String register(@RequestParam String phoneNumber, @RequestParam String otp,
                           @RequestParam(value = "account", required = false)String account,@RequestParam String password,Model model) {
        Map<String,Object>map= otpService.SmsVerifyOTP(phoneNumber, otp);
        if(map.containsKey("success"))
        {
            User user =new User();
            user.setAccount(phoneNumber);user.setPhone(phoneNumber);user.setPassword(password);
            if(account!=null){user.setAccount(account);}
            Map<String, Object> map2 = smsService.register(user);
            if (map2 == null || map2.isEmpty()) {
                model.addAttribute("msg", "注册成功, 快去登录吧!");
                model.addAttribute("target", "/login");
                return "/login";
            } else {
                model.addAttribute("accountMsg", map2.get("accountMsg"));
                model.addAttribute("passwordMsg", map2.get("passwordMsg"));
                model.addAttribute("phoneMsg", map2.get("phoneMsg"));
                return "/register";
            }
        }else{
            model.addAttribute("otpMsg", map.get("fail"));
            return "/register";
        }
    }

    @PostMapping("/resetPwd")
    public Map<String, Object> resetPwd(@RequestParam String phoneNumber, @RequestParam String otp,
                                        @RequestParam String password, Model model) {
        User user = hostHolder.getUser();
        Map<String,Object>map= otpService.SmsVerifyOTP(phoneNumber, otp);
        if(map.containsKey("success"))
        {
            // 执行重置密码操作
            Map<String, Object> stringObjectMap = smsService.doResetPwd(user.getAccount(), password);
            String accountMsg = (String) stringObjectMap.get("errMsg");
            if (StringUtils.isBlank(accountMsg)) {
                map.put("status", "0");
                map.put("msg", "重置密码成功!");
                map.put("target", "/index");
            }
        }else{
            model.addAttribute("otpMsg", map.get("fail"));
        }
        return map;
    }


    @PostMapping("/updatePhone")
    public Map<String, Object> updatePhone(@RequestParam String newPhoneNumber, @RequestParam String otp,
                                          Model model) {
        Map<String,Object>map= otpService.SmsVerifyOTP(newPhoneNumber, otp);
        if(map.containsKey("success"))
        {
            String account= hostHolder.getUser().getAccount();
            // 执行更换手机号操作
            Map<String, Object> stringObjectMap = smsService.updatePhone(account, newPhoneNumber);
            String accountMsg = (String) stringObjectMap.get("errMsg");
            if (StringUtils.isBlank(accountMsg)) {
                map.put("status", "0");
                map.put("msg", "重置手机号成功!");
                map.put("target", "/index");
            }
        }else{
            model.addAttribute("otpMsg", map.get("fail"));
        }
        return map;
    }

}

