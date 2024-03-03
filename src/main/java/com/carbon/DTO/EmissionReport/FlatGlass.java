package com.carbon.DTO.EmissionReport;

import java.util.List;

public class FlatGlass {
    public double eTotal; // 总排放量
    public double eCombustion; // 燃料燃烧排放
    public double emissionsFromtheCombustionoftoner; // 碳粉燃烧产生的CO2排放量
    public double emissionsCarbondioxide; // 碳酸盐分解产生的二氧化碳排放量
    public double eElectricityHeat; // 净购入的电力、热力消费的排放
    //燃烧材料排放
    public List<FuelData> fuelList; // 存储各种燃料的数据
    // 燃料数据类
    public class FuelData {
        public String fuelType; // 燃料类型
        public double activityData; // 活动水平
        public double ncv;
        public double fuelConsumption;
        public double emissionFactor; // 二氧化碳排放因子
        public double carbonContent;
        public double oxidationFactor;

    }




    //碳粉氧化的排放
    public double tonerConsumption; // 碳粉消耗量
    public double weightedAverageoftheCarboncontentofthetoner; // 碳粉含碳量的加权平均值

    //原料分解的排放
    public List<MaterialData> carbonate; // 存储各种燃料的数据
    public class MaterialData{
       public double theWeightoftheCarbonate; // 碳酸盐的重量
       public double carbonateSpecificEmissionfactors; // 碳酸盐特定的排放因子
       public double calcinationRatioofCarbonates; // 碳酸盐的煅烧比例
    }




    // 电力和热力消费
    public double EpurchaseQuantity; // 电力购入量
    public double EexportQuantity; // 电力外销量
    public double HpurchaseQuantity; // 热力购入量
    public double HexportQuantity; // 热力外销量
    public double netElectricityConsumption; // AD电力
    public double netHeatConsumption; // AD热力
    public double emissionFactorElectricity; // EF电力
    public double emissionFactorHeat; // EF热力
}
