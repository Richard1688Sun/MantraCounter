package com.nemogz.mantracounter.counterStuff.Interfaces;

public interface MasterCounterInterface{

    /**
     * add a new counter
     * @param name name of new counter
     * @return true if new counter was added,
     *          false if counter already exists
     */
    public boolean addCounter(String name);

    /**
     * delete a counter
     * @param name name of counter to remove
     * @return true if counter exists and was removed
     *          false otherwise
     */
    public boolean deleteCounter(String name);

    /**
     * increments the count value by 1
     * checks if the mantra count amount has reached the threshold if yes updates the littleHouse count
     * @param name name of counter to remove
     * @return true if the counter has reached te required limit
     */
    public boolean increment(String name);

    /**
     * Set the count amount to the newCount
     * @param name name of counter to remove
     * @param newCount the new count amount to set
     * @return true if new count positive
     */
    public boolean setCount(String name, int newCount);

    /**
     * Decrements the count value by 1
     * @param name name of counter to remove
     * @return true if name exists and number won't be negative
     */
    public boolean decrement(String name);
}
