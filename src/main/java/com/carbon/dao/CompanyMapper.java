package com.carbon.dao;

import com.carbon.entity.Company;
import com.carbon.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CompanyMapper{
    int insertCompany(Company company);

    Company selectByName(String name);
    Company selectByBossId(int bossId);
}