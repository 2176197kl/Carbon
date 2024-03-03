package com.carbon.controller;

import com.carbon.DTO.EmissionReport.EleAl;
import com.carbon.DTO.EmissionReport.MgSmelt;
import com.carbon.DTO.Response;
import com.carbon.entity.Emission;
import com.carbon.service.EmissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emission")
public class EmissionController {

    @Autowired
    private EmissionService emissionService;
//    @PostMapping("/MgSmelt")
//    public String calculateEmission(@RequestBody MgSmelt mgSmelt) throws JsonProcessingException {
//
//        // 计算每种燃料数据
//        for (MgSmelt.FuelData fuelData : mgSmelt.fuelList) {
//            // 计算活动水平数据
//            fuelData.activityData= fuelData.ncv * fuelData.fuelConsumption;
//            // 计算排放因子数据
//            fuelData.emissionFactor = (fuelData.carbonContent * fuelData.oxidationFactor * 44) / 12;
//            // 计算燃烧排放
//            mgSmelt.eCombustion += fuelData.activityData * fuelData.emissionFactor;
//        }
//
//        //计算能源作为原材料用途的排放
//        mgSmelt.eMaterial= mgSmelt.siliconFurnaceProduction* mgSmelt.emissionFactorSiliconFurnace;
//
//        //计算工业生产过程排放
//        mgSmelt.emissionFactorLimeBurning= mgSmelt.DX*0.478;
//        mgSmelt.eProcess= mgSmelt.emissionFactorLimeBurning* mgSmelt.limeConsumption;
//
//        //计算净购入的电力、热力消费的排放
//        mgSmelt.netElectricityConsumption= mgSmelt.EpurchaseQuantity- mgSmelt.EexportQuantity;
//        mgSmelt.netHeatConsumption= mgSmelt.HpurchaseQuantity- mgSmelt.HexportQuantity;
//        mgSmelt.eElectricityHeat= mgSmelt.netElectricityConsumption* mgSmelt.emissionFactorElectricity
//                                              + mgSmelt.netHeatConsumption* mgSmelt.emissionFactorHeat;
//
//
//        // 累加总排放量
//        mgSmelt.eTotal = mgSmelt.eCombustion+ mgSmelt.eMaterial+
//                                      mgSmelt.eProcess+ mgSmelt.eElectricityHeat;
//
//
//        // 使用ObjectMapper转换为JSON字符串
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(mgSmelt);
//
//        //将报告上传至审核
//        emissionService.finishEmission(json,"镁冶炼");
//        return json;
//    }
    @PostMapping("calculateEleAl")
    public String calculateEleAl(@RequestBody EleAl eleAl) throws JsonProcessingException {
        // 使用ObjectMapper转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(eleAl);

        //将报告上传至审核
        emissionService.finishEmission(json,"电解铝");
        return json;
    }
    @PostMapping("calculateMgSmelt")
    public String calculateMgSmelt(@RequestBody EleAl eleAl) throws JsonProcessingException {
        // 使用ObjectMapper转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(eleAl);

        //将报告上传至审核
        emissionService.finishEmission(json,"镁冶炼");
        return json;
    }
    @PostMapping("calculateFlatGlass")
    public String calculateFlatGlass(@RequestBody EleAl eleAl) throws JsonProcessingException {
        // 使用ObjectMapper转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(eleAl);

        //将报告上传至审核
        emissionService.finishEmission(json,"平板玻璃");
        return json;
    }
    @GetMapping("/status")
    public Emission selectByStatus(@RequestParam int status){
        return emissionService.selectByStatus(status);
    }

    @GetMapping("/type")
    public Emission selectByStatus(@RequestParam String type){
        return emissionService.selectByType(type);
    }

    @GetMapping("/updateStatus")
    public Response updateStatus(@RequestParam int id,@RequestParam int status){
        emissionService.updateStatus(id,status);
        if (status==1){
            return new Response().success(null,"已将报告上链");
        }
        if (status==2){
            return new Response().fail(null,"报告有误，被驳回");
        }
        return new Response().fail(null,"未响应");
    }
}

