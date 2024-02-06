package com.carbon.service;

import com.carbon.dao.CompanyMapper;
import com.carbon.dao.UserMapper;
import com.carbon.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
@Service
public class CompanyService {
    @Autowired
    private CompanyMapper companyMapper;

    public void insertCompany(Company company) {
        companyMapper.insertCompany(company);
    }

    public Company selectByName(String name) {
        return companyMapper.selectByName(name);
    }
}
