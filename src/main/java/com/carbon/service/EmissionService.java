package com.carbon.service;

import com.carbon.dao.CompanyMapper;
import com.carbon.dao.EmissionMapper;
import com.carbon.entity.Emission;
import com.carbon.entity.User;
import com.carbon.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmissionService {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private EmissionMapper emissionMapper;
    @Autowired
    private CompanyMapper companyMapper;

    public void finishEmission(String json,String type){
        Emission emission=new Emission();
        emission.setContent(json);
        emission.setStatus(0);
        emission.setType(type);
        emission.setCompanyId(companyMapper.selectByBossId(hostHolder.getUser().getId()).getId());

        emissionMapper.finishEmission(emission);
    }

    public Emission selectByStatus(int status) {
        return emissionMapper.selectByStatus(status);
    }

    public Emission selectByType(String type) {
        return emissionMapper.selectByType(type);
    }

    public void updateStatus(int id,int status) {
        emissionMapper.updateStatus(id,status);
    }
}
