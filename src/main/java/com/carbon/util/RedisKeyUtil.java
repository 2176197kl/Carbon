package com.carbon.util;

/**
 * 生成 Redis 的 key
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";

    private static final String PREFIX_TICKET = "ticket"; // 登录凭证
    private static final String PREFIX_USER = "user"; // 登录凭证




    /**
     * 登陆凭证
     * @param ticket
     * @return
     */
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    /**
     * 用户信息
     * @param userId
     * @return
     */
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }


}
