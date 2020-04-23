package com.gwt.shared;

import java.io.Serializable;
import java.util.List;

public class DTOrequest implements Serializable {
   private List<Integer> integerList;
   private int numberAlgorithm;
   private boolean isIncreasing;

    public DTOrequest() {
    }

    public DTOrequest(List<Integer> integerList, int numberAlgorithm, boolean isIncreasing) {
        this.integerList = integerList;
        this.numberAlgorithm = numberAlgorithm;
        this.isIncreasing = isIncreasing;
    }

    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public int getNumberAlgorithm() {
        return numberAlgorithm;
    }

    public void setNumberAlgorithm(int numberAlgorithm) {
        this.numberAlgorithm = numberAlgorithm;
    }

    public boolean isIncreasing() {
        return isIncreasing;
    }

    public void setIncreasing(boolean increasing) {
        isIncreasing = increasing;
    }

    @Override
    public String toString() {
        return "{" +
                integerList +
                ", numberAlgorithm=" + numberAlgorithm +
                ", isIncreasing=" + isIncreasing +
                '}';
    }
}
