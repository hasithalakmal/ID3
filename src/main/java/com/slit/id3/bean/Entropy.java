package com.slit.id3.bean;

public class Entropy {
    private String property;
    private int numberOfPositives;
    private int numberOfNegatives;
    private int totalResponses;
    private double entropy;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public int getNumberOfPositives() {
        return numberOfPositives;
    }

    public void setNumberOfPositives(int numberOfPositives) {
        this.numberOfPositives = numberOfPositives;
    }

    public int getNumberOfNegatives() {
        return numberOfNegatives;
    }

    public void setNumberOfNegatives(int numberOfNegatives) {
        this.numberOfNegatives = numberOfNegatives;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public int getTotalResponses() {
        return totalResponses;
    }

    public void setTotalResponses(int totalResponses) {
        this.totalResponses = totalResponses;
    }

    public int increaseNumberOfPositiveResponse(){
        this.numberOfPositives++;
        return this.numberOfPositives;
    }

    public int increaseNumberOfNegativeResponse(){
        this.numberOfNegatives++;
        return this.numberOfNegatives;
    }
}
