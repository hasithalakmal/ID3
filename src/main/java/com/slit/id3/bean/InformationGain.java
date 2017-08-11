package com.slit.id3.bean;

import java.util.Map;

public class InformationGain {
    private String criteria;
    private Map<String,Entropy> mapOfEntropy;
    private double informationGain;

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public Map<String, Entropy> getMapOfEntropy() {
        return mapOfEntropy;
    }

    public void setMapOfEntropy(Map<String, Entropy> mapOfEntropy) {
        this.mapOfEntropy = mapOfEntropy;
    }

    public double getInformationGain() {
        return informationGain;
    }

    public void setInformationGain(double informationGain) {
        this.informationGain = informationGain;
    }
}
