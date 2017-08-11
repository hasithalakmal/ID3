package com.slit.id3.bean;

import java.util.Map;

public class CalculationLevel {
    private int levelNumber;
    private Map<String, InformationGain> listOfInformationGain;
    private DataSet dataSet;
    private ClassificationEntropy classificationEntropy;

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public Map<String, InformationGain> getListOfInformationGain() {
        return listOfInformationGain;
    }

    public void setListOfInformationGain(Map<String, InformationGain> listOfInformationGain) {
        this.listOfInformationGain = listOfInformationGain;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public ClassificationEntropy getClassificationEntropy() {
        return classificationEntropy;
    }

    public void setClassificationEntropy(ClassificationEntropy classificationEntropy) {
        this.classificationEntropy = classificationEntropy;
    }
}
