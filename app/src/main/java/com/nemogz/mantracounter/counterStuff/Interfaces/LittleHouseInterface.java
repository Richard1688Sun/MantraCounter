package com.nemogz.mantracounter.counterStuff.Interfaces;

public interface LittleHouseInterface {

    /**
     * Given a mantra name, increment that mantra by 1
     * @param name name of the mantra
     * @return number of times littleHouse was completed, else returns -1
     * @modifies the littleHouse map but not littleHouse Count
     */
    public int incrementCount(String name);

    /**
     * Decrements the littleHouseCount
     * @return true if successful, false else
     */
    public boolean decrementLittleHouseCount();

    /**
     * Sets the the littleHouseCount number
     * @param newCount new count number
     * @return true if successful, false else
     */
    public boolean setLittleCount(int newCount);

    /**
     * Return the completed amount of a mantra by name
     * @param mantra name of the mantra
     * @return count associtated to the mantra
     */
    public double getCountByName(String mantra);

    /**
     * Reset all the counter values in littleHouseMap
     * @return true if successful, false otherwise
     */
    public boolean resetLittleHouse();

}
