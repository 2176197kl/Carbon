package com.carbon.DTO.EmissionReport;

import java.util.List;

public class EleAl {
    public double eTotal; // 总排放量
    public double eCombustion; // 燃料燃烧排放
    public double eMaterial; // 能源作为原材料用途的排放
    public double eProcess; // 工业生产过程排放
    public double eElectricityHeat; // 净购入的电力、热力消费的排放
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
    public double  eEissionfactorConsumedbycarbonanodes; // 炭阳极消耗的二氧化碳排放因子
    public double  productionPrimaryAluminum; // 原铝产量
    public double  consumptionPertonofAluminum; // 吨铝炭阳极净耗
    public double  averageSulfurContent; // 炭阳极平均含硫量
    public double  averageAshContent; // 炭阳极平均灰分含量


    // 工业生产过程排放
    // 1.阳极效应
    public double  averageDurationPerday; // 平均每天每槽阳极效应持续时间
    public double  eEissionfactorCF4; // 阳极效应的CF4排放因子
    public double  eEissionfactorC2F6; // 阳极效应的C2F6排放因子
    public double  Paluminum; // 原铝产量
    public double  eProcessOne; // 工业生产过程阳极效应排放
    // 2.煅烧石灰石
    public double  limestoneConsumption; // 石灰石消耗量
    public double  eEissionfactorLimestonenull; // 石灰石排放因子
    public double  eProcessTwo; // 工业生产过程煅烧石灰石排放

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
