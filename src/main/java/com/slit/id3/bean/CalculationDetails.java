package com.slit.id3.bean;

import java.util.Map;

public class CalculationDetails {
    private Map<Integer,CalculationLevel> calculationLevelMap;

    public Map<Integer, CalculationLevel> getCalculationLevelMap() {
        return calculationLevelMap;
    }

    public void setCalculationLevelMap(Map<Integer, CalculationLevel> calculationLevelMap) {
        this.calculationLevelMap = calculationLevelMap;
    }
}
