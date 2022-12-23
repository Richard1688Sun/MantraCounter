package com.nemogz.mantracounter.counterStuff.Interfaces;

public interface CounterInterface {

    /**
     * increments the count value by 1
     * checks if the mantra count amount has reached the threshold if yes updates the littleHouse count
     * @return true if the counter has reached te required limit
     */
    public boolean increment();

    /**
     * Set the count amount to the newCount
     * @param newCount the new count amount to set
     * @return true if new count positive
     */
    public boolean setCount(int newCount);

    /**
     * Decrements the count value by 1
     * @return true if name exists and number won't be negative
     */
    public boolean decrement();

}
