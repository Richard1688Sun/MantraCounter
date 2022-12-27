package com.nemogz.mantracounter.counterStuff;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.nemogz.mantracounter.MainActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Interfaces.MasterCounterInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class MasterCounter implements MasterCounterInterface {

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

    @Ignore
    private final Context context;

    @Ignore
    private LittleHouse littleHouse;

    @Ignore
    private List<Counter> counters;

    @PrimaryKey
    @NotNull
    private String id = "masterCounter";

    private Integer positionCounters;

    public MasterCounter(Context context) {
        this.context = context;
        this.littleHouse = new LittleHouse(context);
        this.counters = new ArrayList<>();
        this.positionCounters = 0;
        this.dabei = context.getString(R.string.dabei);
        this.boruo = context.getString(R.string.boruo);
        this.wangshen = context.getString(R.string.wangshen);
        this.qifo = context.getString(R.string.qifo);
    }

    //made for the sqlite I don't know why
    public MasterCounter(Integer positionCounters) {
        this.context = null;
        this.littleHouse = new LittleHouse(0);
        this.counters = new ArrayList<>();
        this.positionCounters = positionCounters;
        this.dabei = null;
        this.boruo = null;
        this.wangshen = null;
        this.qifo = null;
    }

    public MasterCounter(Context context, LittleHouse littleHouse, List<Counter> counters, int positionCounters) {
        this.context = context;
        this.littleHouse = littleHouse;
        this.counters = counters;
        this.positionCounters = positionCounters;
        this.dabei = context.getString(R.string.dabei);
        this.boruo = context.getString(R.string.boruo);
        this.wangshen = context.getString(R.string.wangshen);
        this.qifo = context.getString(R.string.qifo);
    }

    @Override
    public boolean increment(String name, boolean autoCountLittleHouse) {
        for (Counter counter: counters) {
            if (counter.getOriginalName().equals(name)) {
                if (littleHouse.getLittleHouseMap().containsKey(name)) {
                    if (counter.increment(littleHouse.getLittleHouseMap().get(name).intValue())) {
                        //still updates the littleHouse map just no conversions to littleHouseCount
                        int completedLittleHouses = littleHouse.incrementCount(name);
                        if (autoCountLittleHouse) {
                            if (completedLittleHouses != 0) {
                                //changes the littleHouse amount
                                littleHouse.updateLittleHouseMapAndCount(completedLittleHouses);
                                for (int i = 0; i < 4; i++) {
                                    if(!counters.get(i).updateCounter(completedLittleHouses)) return false;
                                }
                                return true;
                            }
                        }
                    }
                }
                else { //when counter is created by the user
                    counter.increment(0);
                }
            }
        }

        return false;
    }

    @Override
    public boolean setCount(String name, int newCount) {
        for (Counter counter: counters) {
            if (counter.getOriginalName().equals(name)) {
                return counter.setCount(newCount);
            }
        }
        return false;
    }

    @Override
    public boolean decrement(String name) {
        for (Counter counter: counters) {
            if (counter.getOriginalName().equals(name)) {
                if (counter.decrement()) {
                    int newCompletes = counter.getNumberOfCompletes();
                    if (littleHouse.getLittleHouseMap().containsKey(counter.getOriginalName()) && newCompletes != littleHouse.getCountByName(counter.getOriginalName())) {
                        littleHouse.setLittleHouseByName(counter.getOriginalName(), newCompletes);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addCounter(String name) {
        Counter counter = new Counter(name, 0, context);

        if (!counters.contains(counter)) {
            counters.add(counter);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCounter(String name) {

        for (Counter counter: counters) {
            if(counter.getOriginalName().equals(name)) {
                counters.remove(counter);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean resetCountersAndLittleHouse() {

        for(Counter counter: counters) {
            counter.setCount(0);
        }
        littleHouse.resetLittleHouse();
        return true;
    }

    public LittleHouse getLittleHouse() {
        return littleHouse;
    }

    public List<Counter> getCounters() {
        return counters;
    }

    /**
     * adding the default counters (dabei, boruo, xiaozai, qifo)
     */
    public void createBasicCounters() {
        counters.add(new Counter(dabei, 0, context));
        counters.add(new Counter(boruo, 0, context));
        counters.add(new Counter(wangshen, 0, context));
        counters.add(new Counter(qifo, 0, context));
    }

    public void setLittleHouse(LittleHouse littleHouse) {
        this.littleHouse = littleHouse;
    }

    public void setCounters(List<Counter> counters) {
        this.counters = counters;
    }

    public int getPositionCounters() {
        return positionCounters;
    }

    public void setPositionCounters(int positionCounters) {
        this.positionCounters = positionCounters;
    }

    public void incrementPositionCounter(){
        if(positionCounters == counters.size()-1){
            positionCounters = 0;
        }
        else{
            positionCounters++;
        }
    }

    public void decrementPositionCounter(){
        if(positionCounters == 0){
            positionCounters = counters.size() - 1;
        }
        else{
            positionCounters--;
        }
    }

    public Counter getCounterAtPosition() {
        return counters.get(positionCounters);
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }
}
