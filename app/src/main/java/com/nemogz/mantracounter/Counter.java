package com.nemogz.mantracounter;

import java.util.HashMap;
import java.util.Map;

public class Counter {

    private static final int DaBeiLimit = 27;
    private static final int BoRuoLimit = 49;
    private static final int XiaoZhaiLimit = 84;
    private static final int QiFoLimit = 87;
    private static final String dabei = MainActivity.getAppResources().getString(R.string.dabei);
    private static final String boruo = MainActivity.getAppResources().getString(R.string.boruo);
    private static final String xiaozai = MainActivity.getAppResources().getString(R.string.xiaozai);
    private static final String qifo = MainActivity.getAppResources().getString(R.string.qifo);
    private static Map<String, Integer> littleHouse = new HashMap<>();


    private String name;
    private Integer count;

    public Counter(String name, Integer count) {
        this.name = name;
        this.count = count;
        littleHouse = new HashMap<>();
    }

    /**
     * Initializes the LittleHouse map
     */
    public static void createLittleHouse(){
        littleHouse.put(dabei, 0);
        littleHouse.put(boruo, 0);
        littleHouse.put(xiaozai, 0);
        littleHouse.put(qifo, 0);
        littleHouse.put(MainActivity.getAppResources().getString(R.string.xiaofangzi), 0);
    }

    /**
     * increments the count value by 1
     * checks if the mantra count amount has reached the threshold if yes updates the littleHouse count
     */
    public void increment(){
        count++;

        //check if each mantra count is satisfied
        if((name.equals(dabei)) && (count >= DaBeiLimit)){
            littleHouse.put(dabei, littleHouse.get(dabei)+1);
            updateLittleHouse();
            count = count - DaBeiLimit;
        }
        else if(name.equals(boruo) && count >= BoRuoLimit){
            littleHouse.put(boruo, littleHouse.get(boruo) + 1);
            updateLittleHouse();
            count = count - BoRuoLimit;
        }
        else if(name.equals(xiaozai) && count >= XiaoZhaiLimit){
            littleHouse.put(xiaozai, littleHouse.get(xiaozai) + 1);
            updateLittleHouse();
            count = count - XiaoZhaiLimit;
        }
        else if(name.equals(qifo) && count >= QiFoLimit){
            littleHouse.put(qifo, littleHouse.get(qifo) + 1);
            updateLittleHouse();
            count = count - QiFoLimit;
        }

    }

    /**
     * Set the count amount to the newCount
     * @param newCount the new count amount to set
     */
    public void setCount(int newCount){
        count = newCount;
    }

    /**
     * Decrements the count value by 1
     */
    public void decrement(){
        count--;
    }

    /**
     * Update the littleHouse count if the difference mantras reached the recitation threshold
     * Also decrements the mantra threshold amount
     */
    private static void updateLittleHouse(){
        if((littleHouse.get(dabei) >= 1) && littleHouse.get(boruo) >= 1 && littleHouse.get(xiaozai) >= 1 && littleHouse.get(qifo) >= 1){
            littleHouse.put(MainActivity.getAppResources().getString(R.string.xiaofangzi), littleHouse.get(MainActivity.getAppResources().getString(R.string.xiaofangzi))+1);
            littleHouse.put(dabei, littleHouse.get(dabei) - 1);
            littleHouse.put(boruo, littleHouse.get(boruo) - 1);
            littleHouse.put(xiaozai, littleHouse.get(xiaozai) - 1);
            littleHouse.put(qifo, littleHouse.get(qifo) - 1);
        }
    }

    public static Map<String, Integer> getLittleHouse() {
//        Map<String, Integer> mapReturn = new HashMap<>(littleHouse);
//        return mapReturn;
        return littleHouse;
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }
}
