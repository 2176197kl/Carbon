package com.carbon.entity;

import java.util.Date;
import java.util.List;

public class Company {
    private int id;
    private int bossId;
    private String name;
    private String adress;
    private List<String> proof;
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }

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

    public List<String> getProof() {
        return proof;
    }

    public void setProof(List<String> proof) {
        this.proof = proof;
    }


}
