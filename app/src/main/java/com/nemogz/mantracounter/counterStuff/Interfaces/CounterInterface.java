package com.nemogz.mantracounter.counterStuff.Interfaces;

public interface CounterInterface {

    /**
     * increments the count value by 1
     * @return true if the counter has reached te required limit
     * @param completedAmount number of times the counter completed the littleHouse amount
     */
    public boolean increment(int completedAmount);

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

    /**
     * Subtracts the LittleHouse amount from the counter
     * @requires the littleHouse to have incremented successfully
     * @param timesToUpdate number of littleHouse that have been completed
     * @return true if an update was made, false otherwise
     */
    public boolean updateCounter(int timesToUpdate);
}
