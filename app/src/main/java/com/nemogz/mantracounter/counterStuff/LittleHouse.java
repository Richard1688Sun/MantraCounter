package com.nemogz.mantracounter.counterStuff;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.nemogz.mantracounter.MainActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Interfaces.LittleHouseInterface;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Entity
public class LittleHouse implements LittleHouseInterface {

    /**
     * RI:
     *  - all counts must positive
     */
    @Ignore
    private static final int DaBeiLimit = 27;
    @Ignore
    private static final int BoRuoLimit = 49;
    @Ignore
    private static final int XiaoZhaiLimit = 84;
    @Ignore
    private static final int QiFoLimit = 87;
    @Ignore
    private final String dabei;
    @Ignore
    private final String boruo;
    @Ignore
    private final String wangshen;
    @Ignore
    private final String qifo;

    private Map<String, Double> littleHouseMap;

    @PrimaryKey
    @NotNull
    private Integer littleHouseCount;

//    public LittleHouse(String dabei, String boruo, String wangshen, String qifo) {
//        this.littleHouseMap = new HashMap<>();
//        this.dabei = dabei;
//        this.boruo = boruo;
//        this.wangshen = wangshen;
//        this.qifo = qifo;
//        this.littleHouseCount = 0;
//        this.littleHouseMap.put(dabei, 0);
//        this.littleHouseMap.put(boruo, 0);
//        this.littleHouseMap.put(wangshen, 0);
//        this.littleHouseMap.put(qifo, 0);
//    }

    public LittleHouse(@NonNull Integer littleHouseCount) {
        this.littleHouseMap = new HashMap<>();
        this.dabei = null;
        this.boruo = null;
        this.wangshen = null;
        this.qifo = null;
        this.littleHouseCount = littleHouseCount;
        this.littleHouseMap.put(dabei, 0.0);
        this.littleHouseMap.put(boruo, 0.0);
        this.littleHouseMap.put(wangshen, 0.0);
        this.littleHouseMap.put(qifo, 0.0);
    }

    public LittleHouse(Context context) {
        this.littleHouseMap = new HashMap<>();
        this.dabei = context.getString(R.string.dabei);
        this.boruo = context.getString(R.string.boruo);
        this.wangshen = context.getString(R.string.wangshen);
        this.qifo = context.getString(R.string.qifo);
        this.littleHouseCount = 0;
        this.littleHouseMap.put(dabei, 0.0);
        this.littleHouseMap.put(boruo, 0.0);
        this.littleHouseMap.put(wangshen, 0.0);
        this.littleHouseMap.put(qifo, 0.0);
    }


    @Override
    public boolean incrementCount(String name) {
        if(littleHouseMap.containsKey(name)) {
            double pastNum = littleHouseMap.get(name);
            littleHouseMap.put(name, pastNum + 1.0);
            return true;
        }
        return false;
    }

    @Override
    public boolean decrementLittleHouseCount() {
        if(littleHouseCount - 1 >= 0){
            littleHouseCount = littleHouseCount - 1;
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
    public double getCountByName(String mantra) {
        return littleHouseMap.get(mantra).doubleValue();
    }

    /**
     * update the littleHouse count
     * @return true littleHouse was updated, false otherwise
     */
    private boolean updateLittleHouse() {
        double minCount = Double.MAX_VALUE;

        //find the completion count out of all of mantras
        for(String mantra: littleHouseMap.keySet()) {
            if(littleHouseMap.get(mantra) < minCount) {
                minCount = littleHouseMap.get(mantra);
            }
        }

        //subtracts the minvalue
        for(String mantra: littleHouseMap.keySet()) {
            littleHouseMap.put(mantra, littleHouseMap.get(mantra) - minCount);
        }

        if(minCount > 0) {
            littleHouseCount = littleHouseCount + (int)minCount;
            return true;
        }
        return false;
    }

    public Integer getLittleHouseCount() {
        updateLittleHouse();
        return littleHouseCount;
    }

    public Map<String, Double> getLittleHouseMap() {
        return littleHouseMap;
    }

    public void setLittleHouseCount(@NotNull Integer littleHouseCount) {
        this.littleHouseCount = littleHouseCount;
    }

    public void setLittleHouseMap(Map<String, Double> littleHouseMap) {
        this.littleHouseMap = littleHouseMap;
    }
}
