package com.nemogz.mantracounter.counterStuff;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.nemogz.mantracounter.MainActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Interfaces.MasterCounterInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private final Context context;

    private LittleHouse littleHouse;
    private List<Counter> counters;

    public MasterCounter(Context context) {
        this.context = context;
        this.littleHouse = new LittleHouse(context);
        this.counters = new ArrayList<>();
        this.dabei = context.getString(R.string.dabei);
        this.boruo = context.getString(R.string.boruo);
        this.wangshen = context.getString(R.string.wangshen);
        this.qifo = context.getString(R.string.qifo);
    }

    @Override
    public boolean increment(String name) {
        for (Counter counter: counters) {
            if (counter.getName().equals(name)) {
                if (counter.increment()) {
                    return littleHouse.incrementCount(name);
                }
            }
        }
        return false;
    }

    @Override
    public boolean setCount(String name, int newCount) {
        for (Counter counter: counters) {
            if (counter.getName().equals(name)) {
                return counter.setCount(newCount);
            }
        }
        return false;
    }

    @Override
    public boolean decrement(String name) {
        for (Counter counter: counters) {
            if (counter.getName().equals(name)) {
                return counter.decrement();
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
            if(counter.getName().equals(name)) {
                counters.remove(counter);
                return true;
            }
        }
        return false;
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
}
