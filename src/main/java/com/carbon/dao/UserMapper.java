package com.carbon.dao;

import com.carbon.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    /**
     * 根据 id 查询用户
     * @param id
     * @return
     */
    User selectById (int id);

    /**
     * 根据 username 查询用户
     * @param username
     * @return
     */
    User selectByName(String username);


    /**
     * 插入用户（注册）
     * @param user
     * @return
     */
    int insertUser(User user);



    /**
     * 修改头像
     * @param id
     * @param headerUrl
     * @return
     */
    int updateHeader(int id, String headerUrl);

    /**
     * 修改密码
     * @param id
     * @param password 新密码
     * @return
     */
    int updatePassword(int id, String password);

    int updatePhone(int id,String newPhoneNumber);
}
