package com.carbon.controller;

import com.carbon.DTO.CompanyDTO;
import com.carbon.DTO.Response;
import com.carbon.entity.Company;
import com.carbon.util.HostHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiniu.util.StringMap;
import com.carbon.entity.User;
import com.carbon.service.*;
import com.carbon.util.CarbonConstant;
import com.carbon.util.CarbonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * 用户
 */
@Controller
@RequestMapping("/user")
public class UserController implements CarbonConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private ImgService imgService;



    // 网站域名
    @Value("${carbon.path.domain}")
    private String domain;

    // 项目名(访问路径)
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 进入注册界面
     * @return
     */
    @GetMapping("/register")
    public String getRegisterPage() {
        return "/register";
    }

    /**
     * 进入登录界面
     * @return
     */
    @GetMapping("/login")
    public String getLoginPage() {
        return "/login";
    }

    /**
     * 进入用户信息修改界面
     */
    @GetMapping("/resetUser")
    public String getResetPwdPage() {
        return "/resetUser";
    }

    /**
     * 注册用户
     * @param model
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功, 快去登录吧!");
            model.addAttribute("target", "/index");
            return "/login";
        } else {
            model.addAttribute("accountMsg", map.get("accountMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("phoneMsg", map.get("phoneMsg"));
            return "/register";
        }
    }







    /**
     * 用户登录
     * @param account 用户名
     * @param password 密码
     * @param model
     * @param response
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam("account") String account,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
                        Model model, HttpServletResponse response
    )
    {



        // 凭证过期时间（是否记住我）
        int expiredSeconds = rememberMe ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        // 验证用户名和密码
        Map<String, Object> map = userService.login(account, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            // 账号和密码均正确，则服务端会生成 ticket，浏览器通过 cookie 存储 ticket
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath); // cookie 有效范围
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }
        else {
            model.addAttribute("accountMsg", map.get("accountMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/login";
        }

    }

    /**
     * 用户登出
     * @param ticket 设置凭证状态为无效
     * @return
     */
    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }

    /**
     * 跳转至账号设置界面
     * @return
     */
    @GetMapping("/setting")
    public String getSettingPage(Model model) {
        // 生成上传文件的名称
        String fileName = CarbonUtil.generateUUID();
        model.addAttribute("fileName", fileName);

        // 设置响应信息(aliyun 的规定写法)
        StringMap policy = new StringMap();
        policy.put("returnBody", CarbonUtil.getJSONString(0));
        // 生成上传到 aliyun 的凭证(aliyun 的规定写法)
//        Auth auth = Auth.create(acc, secretKey);
//        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);
//        model.addAttribute("uploadToken", uploadToken);

        return "/setting";
    }




    /**
     * 更新图像路径（将本地的图像路径更新为云服务器上的图像路径）
     * @param file
     * @return
     */
//    @PostMapping("/header/url")
//    @ResponseBody
//    public String updateHeaderUrl(String fileName) {
//        if (StringUtils.isBlank(fileName)) {
//            return CarbonUtil.getJSONString(1, "文件名不能为空");
//        }
//
//        // 文件在云服务器上的的访问路径
//        String url = headerBucketUrl + "/" + fileName;
//        userService.updateHeader(hostHolder.getUser().getId(), url);
//
//        return CarbonUtil.getJSONString(0);
//
//    }

    @PostMapping("/header/url")
    @ResponseBody
    public Map<String, Object> upload(@RequestPart("file") MultipartFile file){
        String imgFileStr = imgService.upload(file);
        if(imgFileStr!=null){
            userService.updateHeader(hostHolder.getUser().getId(), imgFileStr);
            //userService.updateHeader(2, imgFileStr);
        }
        return buildResult(imgFileStr);
    }
    private Map<String,Object> buildResult(String str){
        Map<String, Object> result = new HashMap<>();
        //判断字符串用lang3下的StringUtils去判断，这块我就不引入新的依赖了
        if(str== null || "".equals(str)){
            result.put("code",10000);
            result.put("msg","图片上传失败");
            result.put("data",null);
        }else{
            result.put("code",200);
            result.put("msg","图片上传成功");
            result.put("data",str);
        }
        return result;
    }


    /**
     * 修改用户密码
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @param model
     * @return
     */
    @PostMapping("/resetPwd")
    public String updatePassword(String oldPassword, String newPassword, Model model) {
        // 验证原密码是否正确
        User user = hostHolder.getUser();
        String md5OldPassword = CarbonUtil.md5(oldPassword);
        if (!user.getPassword().equals(md5OldPassword)) {
            model.addAttribute("oldPasswordError", "原密码错误");
            return "原密码错误";
        }

        // 判断新密码是否合法
        String md5NewPassword = CarbonUtil.md5(newPassword);
        if (user.getPassword().equals(md5NewPassword)) {
            model.addAttribute("newPasswordError", "新密码和原密码相同");
            return "新密码和原密码相同";
        }

        // 修改用户密码
        userService.updatePassword(user.getId(), newPassword);

        return "密码修改成功";
    }



    //审核公司绑定
    @PostMapping
    public Response auditBindCompany(){return new Response().success("绑定成功",null);}


    //审核公司绑定
    @PostMapping
    public Response applyBindCompany(){return new Response().success("已申请",null);}
}
