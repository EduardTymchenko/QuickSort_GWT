package com.gwt.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DTOresponse implements Serializable {
    private int i;
    private int j;
    private int indexPaviot;
    private int low;
    private int high;
    private boolean isSwap;
    private List<Integer> integerList;

    public DTOresponse(){}

    public DTOresponse(int low, int high, int i, int j, int indexPaviot, boolean isSwap, List<Integer> integerList) {
        this.low = low;
        this.high = high;
        this.i = i;
        this.j = j;
        this.indexPaviot = indexPaviot;
        this.isSwap = isSwap;
        this.integerList = new ArrayList<>(integerList);
    }

    public DTOresponse(int low, int high, int i, int j, int indexPaviot, List<Integer> integerList) {
        this.low = low;
        this.high = high;
        this.i = i;
        this.j = j;
        this.indexPaviot = indexPaviot;
        this.integerList = new ArrayList<>(integerList);
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getIndexPaviot() {
        return indexPaviot;
    }

    public void setIndexPaviot(int indexPaviot) {
        this.indexPaviot = indexPaviot;
    }

    public boolean isSwap() {
        return isSwap;
    }

    public void setSwap(boolean swap) {
        isSwap = swap;
    }

    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return "{" + integerList +
                " low=" + low +
                " high=" + high +
                " i=" + i +
                ", j=" + j +
                ", indexPaviot=" + indexPaviot +
                ", isSwap=" + isSwap +
                "}" +
                "\n";
    }
}
