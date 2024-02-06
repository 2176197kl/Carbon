package com.carbon.dao;

import com.carbon.entity.Emission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EmissionMapper {
     void finishEmission(Emission emission);


     Emission selectById(int Id);
}
