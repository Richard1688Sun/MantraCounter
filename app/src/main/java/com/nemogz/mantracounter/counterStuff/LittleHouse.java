package com.nemogz.mantracounter.counterStuff;

import com.nemogz.mantracounter.MainActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Interfaces.LittleHouseInterface;

import java.util.HashMap;
import java.util.Map;

public class LittleHouse implements LittleHouseInterface {

    /**
     * RI:
     *  - all counts must positive
     */
    private static final int DaBeiLimit = 27;
    private static final int BoRuoLimit = 49;
    private static final int XiaoZhaiLimit = 84;
    private static final int QiFoLimit = 87;
    private static final String dabei = MainActivity.getAppResources().getString(R.string.dabei);
    private static final String boruo = MainActivity.getAppResources().getString(R.string.boruo);
    private static final String wangshen = MainActivity.getAppResources().getString(R.string.wangshen);
    private static final String xiaozai = MainActivity.getAppResources().getString(R.string.xiaozai);
    private static final String qifo = MainActivity.getAppResources().getString(R.string.qifo);
    private static final String xiaofangzi = MainActivity.getAppResources().getString(R.string.xiaofangzi);

    private final Map<String, Integer> littleHouse;
    private Integer littleHouseCount;

    public LittleHouse() {
        this.littleHouse = new HashMap<>();
        littleHouseCount = 0;
        littleHouse.put(dabei, 0);
        littleHouse.put(boruo, 0);
        littleHouse.put(wangshen, 0);
        littleHouse.put(qifo, 0);
    }


    @Override
    public boolean incrementCount(String name) {
        if(littleHouse.containsKey(name)) {
            int pastNum = littleHouse.get(name);
            littleHouse.put(name, pastNum+1);
            return true;
        }
        return false;
    }

    @Override
    public boolean decrementLittleHouseCount() {
        if(littleHouseCount - 1 >= 0){
            littleHouseCount = littleHouseCount -1;
            return true;
        }
        return false;
    }

    @Override
    public boolean setLittleCount(int newCount) {
        if(newCount >= 0){
            littleHouseCount = newCount;
            return true;
        }
        return false;
    }

    @Override
    public int getCountByName(String mantra) {
        return littleHouse.get(mantra);
    }

    /**
     * update the littleHouse count
     * @return true littleHouse was updated, false otherwise
     */
    private boolean updateLittleHouse() {
        int minCount = Integer.MAX_VALUE;

        //find the completion count out of all of mantras
        for(String mantra: littleHouse.keySet()) {
            if(littleHouse.get(mantra) < minCount) {
                minCount = littleHouse.get(mantra);
            }
        }

        //subtracts the minvalue
        for(String mantra: littleHouse.keySet()) {
            littleHouse.put(mantra, littleHouse.get(mantra) - minCount);
        }

        if(minCount > 0) {
            littleHouseCount = littleHouseCount + minCount;
            return true;
        }
        return false;
    }

    public Map<String, Integer> getLittleHouse() {
        return littleHouse;
    }

    public Integer getLittleHouseCount() {
        updateLittleHouse();
        return littleHouseCount;
    }
}
