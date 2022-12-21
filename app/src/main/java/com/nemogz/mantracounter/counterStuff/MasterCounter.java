package com.nemogz.mantracounter.counterStuff;

import com.nemogz.mantracounter.MainActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Interfaces.MasterCounterInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MasterCounter implements MasterCounterInterface {

    private static final int DaBeiLimit = 27;
    private static final int BoRuoLimit = 49;
    private static final int XiaoZhaiLimit = 84;
    private static final int QiFoLimit = 87;
    private static final String dabei = MainActivity.getAppResources().getString(R.string.dabei);
    private static final String boruo = MainActivity.getAppResources().getString(R.string.boruo);
    private static final String wangshen = MainActivity.getAppResources().getString(R.string.wangshen);
    private static final String qifo = MainActivity.getAppResources().getString(R.string.qifo);

    private LittleHouse littleHouse;
    private List<Counter> counters;

    public MasterCounter() {
        this.littleHouse = new LittleHouse();
        this.counters = new ArrayList<>();
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
        Counter counter = new Counter(name, 0);

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
        counters.add(new Counter(dabei, 0));
        counters.add(new Counter(boruo, 0));
        counters.add(new Counter(wangshen, 0));
        counters.add(new Counter(qifo, 0));
    }
}
