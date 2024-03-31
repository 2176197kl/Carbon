package com.carbon.dao;

import com.carbon.entity.Emission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EmissionMapper {
     void finishEmission(Emission emission);


     Emission selectById(int Id);

    List<Emission> selectByStatus(int status);

    List<Emission> selectByType(String type);

    void updateStatus(int id,int status,int auditor_id);
}
