package org.cpd.shared;

public class Rank {
    public static final int DEFAULT_OFFSET = 50;
    public static final int DEFAULT_START = 100;
    private int value;
    private final int upperBound;
    private final int lowerBound;

    public Rank(){
        this(DEFAULT_START);
    }

    public Rank(int value){
        this(value, DEFAULT_OFFSET);
    }

    public Rank(int value, int offset){
        this.value = value;
        this.upperBound = this.value + offset;
        this.lowerBound = this.value + offset;
    }

    public void add(int value){
        this.value += value;
    }

    public int getValue(){
        return this.value;
    }

    public boolean isInBounds(int value){
        return value >= lowerBound && value <= upperBound;
    }

}
