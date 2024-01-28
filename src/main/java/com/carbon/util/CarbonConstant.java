package com.carbon.util;

/**
 * 常量
 */
public interface CarbonConstant {



    // 系统用户的 id
    int SYSTEM_USER_ID = 1;

    // 权限：企业
    String AUTHORITY_ENTERPRISE = "enterprise";
    // 权限：数据审核员
    String AUTHORITY_DATA_AUDITOR = "data_auditor";
    // 权限：第三方监管机构
    String AUTHORITY_THIRD_PARTY_REGULATOR = "third_party_regulator";
    // 权限：管理员
    String AUTHORITY_ADMIN = "admin";
    // 默认的登录凭证超时时间 (12小时)
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    // 记住我状态下的凭证超时时间 (100天)
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
}
