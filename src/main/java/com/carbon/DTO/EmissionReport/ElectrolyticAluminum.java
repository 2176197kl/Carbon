package com.carbon.DTO.EmissionReport;

import java.util.List;

public class ElectrolyticAluminum {
    public double eTotal; // 总排放量
    public double eCombustion; // 燃料燃烧排放
    public double eMaterial; // 能源作为原材料用途的排放
    public double eProcess; // 工业生产过程排放
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

    // 能源作为原材料用途
    public double siliconFurnaceProduction; // S
    public double emissionFactorSiliconFurnace; // EF硅铁

    // 工业生产过程排放
    public double emissionFactorLimeBurning; // EF白云石
    public double limeConsumption; // D
    public double DX; // DX


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
