package com.nemogz.mantracounter.counterStuff;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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

    @ColumnInfo
    private Map<String, Double> littleHouseMap;
    @ColumnInfo
    private Integer littleHouseCount;

    @ColumnInfo
    private String littleHouseDisplayName;

    @PrimaryKey
    @NotNull
    private String id = "littleHouse";

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
        this.littleHouseDisplayName = context.getString(R.string.xiaofangzi);
    }


    @Override
    public int incrementCount(String name) {
        if(littleHouseMap.containsKey(name)) {
            double pastNum = littleHouseMap.get(name);
            littleHouseMap.put(name, pastNum + 1.0);
            return findLittleHouseCompleted();
        }
        return 0;
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

    @Override
    public boolean resetLittleHouse() {

        for(String key: littleHouseMap.keySet()) {
            littleHouseMap.put(key, 0.0);
        }

        littleHouseCount = 0;
        return false;
    }

    public boolean resetLittleHouseByName(String name) {
        if (littleHouseMap.containsKey(name)) {
            littleHouseMap.put(name, 0.0);
            return true;
        }
        return false;
    }

    public boolean setLittleHouseByName(String name, int newCount) {
        if (littleHouseMap.containsKey(name)) {
            littleHouseMap.put(name, (double)newCount);
            return true;

        }
        return false;
    }

    public int incrementByValueCount(String name, int count) {
        if(littleHouseMap.containsKey(name)) {
            double pastNum = littleHouseMap.get(name);
            littleHouseMap.put(name, pastNum + count);
            return findLittleHouseCompleted();
        }
        return 0;
    }

    /**
     * update the littleHouse count
     * @return amount of littleHouse updated
     * @modfies the littleHouse map and the littleHouse count
     */
    public int updateLittleHouseMapAndCount() {
        double minCount = findLittleHouseCompleted();

        //subtracts the minvalue
        for(String mantra: littleHouseMap.keySet()) {
            littleHouseMap.put(mantra, littleHouseMap.get(mantra) - minCount);
        }

        if(minCount > 0) {
            littleHouseCount = littleHouseCount + (int)minCount;
            return (int)minCount;
        }
        return 0;
    }

    /**
     * updates the littleHouse map based on the count given
     * @param count the amount of littleHouse completed so subtraction from the map is required
     * @return true if something was subtracted from littleHouse map
     */
    public boolean updateLittleHouseMapAndCount(int count) {
        //subtracts the minvalue
        for(String mantra: littleHouseMap.keySet()) {
            littleHouseMap.put(mantra, littleHouseMap.get(mantra) - (double)count);
        }

        if(count > 0) {
            littleHouseCount = littleHouseCount + count;
            return true;
        }
        return false;
    }

    /**
     * update the littleHouse count
     * @return amount of littleHouse updated
     * @modfies the littleHouse map and the littleHouse count
     */
    public int findLittleHouseCompleted() {
        double minCount = -1.0;

        //find the completion count out of all of mantras
        for(String mantra: littleHouseMap.keySet()) {
            if(littleHouseMap.get(mantra) < minCount || minCount == -1.0) {
                minCount = littleHouseMap.get(mantra);
            }
        }

        if(minCount > 0) {
            return (int)minCount;
        }
        return 0;
    }

    public Integer getLittleHouseCount() {
        return littleHouseCount;
    }

    public Map<String, Double> getLittleHouseMap() {
        return littleHouseMap;
    }

    public void setLittleHouseMap(Map<String, Double> littleHouseMap) {
        this.littleHouseMap = littleHouseMap;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getLittleHouseDisplayName() {
        return littleHouseDisplayName;
    }

    public void setLittleHouseDisplayName(String littleHouseDisplayName) {
        this.littleHouseDisplayName = littleHouseDisplayName;
    }
}
