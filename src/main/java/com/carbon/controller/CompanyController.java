package com.carbon.controller;

import com.carbon.DTO.CompanyDTO;
import com.carbon.DTO.Response;
import com.carbon.entity.Company;
import com.carbon.service.CompanyService;
import com.carbon.service.ImgService;
import com.carbon.service.UserService;
import com.carbon.util.HostHolder;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.carbon.util.CarbonUtil.linkedListToString;

@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private ImgService imgService;
    //填写公司信息
    @PostMapping("/set")
    public Response setCompany(@RequestBody CompanyDTO companyDTO){
        Company company=new Company();
        company.setBossId(hostHolder.getUser().getId());
        company.setName(companyDTO.getName());
        company.setAdress(companyDTO.getAdress());
        //上传证明材料图片
        LinkedList<String> listProof=new LinkedList<>();
        for (MultipartFile file:companyDTO.getProof()) {
            String imgFileStr = imgService.upload(file);
            if(imgFileStr!=null){
                listProof.add(imgFileStr);
            }
        }
        String listAsString = linkedListToString(listProof);
        company.setProof(listAsString);
        companyService.insertCompany(company);
        return new Response().success(null,"上传成功，待审核中");
    }

    @PostMapping("/selectByName")
    public Response selectByName(@RequestBody CompanyDTO companyDTO){
        Company company=companyService.selectByName(companyDTO.getName());
        if(company!=null){
            return new Response().success(company,"查询成功");
        }else{
            return new Response().success(company,"不存在");
        }

    }
}
