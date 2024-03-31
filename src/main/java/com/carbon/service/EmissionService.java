package com.carbon.service;

import com.carbon.dao.CompanyMapper;
import com.carbon.dao.EmissionMapper;
import com.carbon.entity.Emission;
import com.carbon.entity.User;
import com.carbon.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Emission> selectByStatus(int status) {
        return emissionMapper.selectByStatus(status);
    }

    public List<Emission> selectByType(String type) {
        return emissionMapper.selectByType(type);
    }

    public void updateStatus(int id,int status,int auditor_id) {
        emissionMapper.updateStatus(id,status,auditor_id);
    }

    public Emission selectById(int id) {
        return emissionMapper.selectById(id);
    }
}
