package com.carbon.dao;

import com.carbon.entity.Emission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EmissionMapper {
     void finishEmission(Emission emission);


     Emission selectById(int Id);

    Emission selectByStatus(int status);

    Emission selectByType(String type);

    void updateStatus(int id,int status);
}
