package com.slit.id3.bean;

public class ClassificationEntropy {
    private int numberOfTotalPositives;
    private int numberOfTotalNegatives;
    private int numberOfTotalRecodes;
    private double classificationEntropy;

    public int getNumberOfTotalPositives() {
        return numberOfTotalPositives;
    }

    public void setNumberOfTotalPositives(int numberOfTotalPositives) {
        this.numberOfTotalPositives = numberOfTotalPositives;
    }

    public int getNumberOfTotalNegatives() {
        return numberOfTotalNegatives;
    }

    public void setNumberOfTotalNegatives(int numberOfTotalNegatives) {
        this.numberOfTotalNegatives = numberOfTotalNegatives;
    }

    public int getNumberOfTotalRecodes() {
        return numberOfTotalRecodes;
    }

    public void setNumberOfTotalRecodes(int numberOfTotalRecodes) {
        this.numberOfTotalRecodes = numberOfTotalRecodes;
    }

    public double getClassificationEntropy() {
        return classificationEntropy;
    }

    public void setClassificationEntropy(double classificationEntropy) {
        this.classificationEntropy = classificationEntropy;
    }
}
