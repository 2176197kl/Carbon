package com.carbon.service;

import com.carbon.dao.UserMapper;
import com.carbon.entity.User;
import com.carbon.util.CarbonConstant;
import com.carbon.util.CarbonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


import java.util.*;


/**
 * 用户相关
 */
@Service
public class UserService implements CarbonConstant {

    @Autowired
    private UserMapper userMapper;


    // 网站域名
    @Value("${community.path.domain}")
    private String domain;

    // 项目名(访问路径)
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 根据 Id 查询用户
     * @param id
     * @return
     */
    public User findUserById (int id) {
        // return userMapper.selectById(id);
        User user=null;
        return user;
    }

    /**
     * 根据 username 查询用户
     * @param username
     * @return
     */
    public User findUserByName(String username) {
        return userMapper.selectByName(username);
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
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }



        // 验证账号是否已存在
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在");
            return map;
        }



        // 注册用户
        user.setType(0); // 默认普通用户

        // 随机头像（用户登录后可以自行修改）
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date()); // 注册时间
        userMapper.insertUser(user);


        return map;
    }



    /**
     * 用户登录（为用户创建凭证）
     * @param username
     * @param password
     * @param expiredSeconds 多少秒后凭证过期
     * @return Map<String, Object> 返回错误提示消息以及 ticket(凭证)
     */
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在");
            return map;
        }


        // 验证密码
        password = CarbonUtil.md5(password);
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码错误");
            return map;
        }



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
        return rows;
    }

    /**
     * 修改用户密码（对新密码加盐加密存入数据库）
     * @param userId
     * @param newPassword 新密码
     * @return
     */
    public int updatePassword(int userId, String newPassword) {
        User user = userMapper.selectById(userId);
        // 重新加盐加密
        newPassword = CarbonUtil.md5(newPassword);
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
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }





}
