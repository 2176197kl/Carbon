package com.carbon.DTO;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CompanyDTO {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public List<MultipartFile> getProof() {
        return proof;
    }

    public void setProof(List<MultipartFile> proof) {
        this.proof = proof;
    }

    private String adress;
    private List<MultipartFile> proof;
}
