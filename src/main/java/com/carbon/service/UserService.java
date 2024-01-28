package com.carbon.service;

import com.carbon.dao.UserMapper;
import com.carbon.entity.LoginTicket;
import com.carbon.entity.User;
import com.carbon.util.CarbonConstant;
import com.carbon.util.CarbonUtil;
import com.carbon.util.MailClient;
import com.carbon.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 用户相关
 */
@Service
public class UserService implements CarbonConstant {

    @Autowired
    private UserMapper userMapper;


    // 网站域名
    @Value("${carbon.path.domain}")
    private String domain;

    // 项目名(访问路径)
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;
    @Qualifier("mailClient")
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    /**
     * 根据 Id 查询用户
     * @param id
     * @return
     */
    public User findUserById (int id) {
        return userMapper.selectById(id);
    }

    /**
     * 根据 account 查询用户
     * @param account
     * @return
     */
    public User findUserByName(String account) {
        return userMapper.selectByName(account);
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




    /**
     * 修改用户头像
     * @param userId
     * @param headUrl
     * @return
     */
    public int updateHeader(int userId, String headUrl) {
        // return userMapper.updateHeader(userId, headUrl);
        int rows = userMapper.updateHeader(userId, headUrl);
        clearCache(userId);
        return rows;
    }

    /**
     * 修改用户密码
     * @param userId
     * @param newPassword 新密码
     * @return
     */
    public int updatePassword(int userId, String newPassword) {
        User user = userMapper.selectById(userId);
        // 修改密码
        newPassword = CarbonUtil.md5(newPassword);
        clearCache(userId);
        return userMapper.updatePassword(userId, newPassword);
    }





    /**
     * 获取某个用户的权限
     * @param userId
     * @return
     */
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_DATA_AUDITOR;
                    case 2:
                        return AUTHORITY_THIRD_PARTY_REGULATOR;
                    case 3:
                        return AUTHORITY_ADMIN;
                    default:
                        return AUTHORITY_ENTERPRISE;
                }
            }
        });
        return list;
    }


    /**
     * 用户退出（将凭证状态设为无效）
     * @param ticket
     */
    public void logout(String ticket) {
        // loginTicketMapper.updateStatus(ticket, 1);
        // 修改（先删除再插入）对应用户在 redis 中的凭证状态
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    /**
     * 根据 ticket 查询 LoginTicket 信息
     * @param ticket
     * @return
     */
    public LoginTicket findLoginTicket(String ticket) {
        // return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }



    /**
     * 优先从缓存中取值
     * @param userId
     * @return
     */
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 缓存中没有该用户信息时，则将其存入缓存
     * @param userId
     * @return
     */
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    /**
     * 用户信息变更时清除对应缓存数据
     * @param userId
     */
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }



}
