package com.nemogz.mantracounter.counterStuff;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.nemogz.mantracounter.MainActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Interfaces.MasterCounterInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
public class MasterCounter implements MasterCounterInterface {

    @Ignore
    private static final int DaBeiLimit = 27;
    @Ignore
    private static final int BoRuoLimit = 49;
    @Ignore
    private static final int WangShenLimit = 84;
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

    @ColumnInfo
    private Integer positionCounters;
    @ColumnInfo
    private Integer homeworkCount;
    @ColumnInfo
    private String homeworkDisplayName;
    @ColumnInfo
    private String lastHomeworkDateTime;

    public String getLastHomeworkDateTime() {
        return lastHomeworkDateTime;
    }

    public void setLastHomeworkDateTime(String lastHomeworkDateTime) {
        this.lastHomeworkDateTime = lastHomeworkDateTime;
    }

    public MasterCounter(Context context) {
        this.context = context;
        this.littleHouse = new LittleHouse(context);
        this.counters = new ArrayList<>();
        this.positionCounters = 0;
        this.dabei = context.getString(R.string.dabei);
        this.boruo = context.getString(R.string.boruo);
        this.wangshen = context.getString(R.string.wangshen);
        this.qifo = context.getString(R.string.qifo);
        this.homeworkCount = 0;
        this.homeworkDisplayName = context.getString(R.string.dailyHomework);
    }

    //made for the sqlite I don't know why
    public MasterCounter(Integer positionCounters, Integer homeworkCount, String homeworkDisplayName) {
        this.context = null;
        this.littleHouse = new LittleHouse(0);
        this.counters = new ArrayList<>();
        this.positionCounters = positionCounters;
        this.dabei = null;
        this.boruo = null;
        this.wangshen = null;
        this.qifo = null;
        this.homeworkCount = homeworkCount;
        this.homeworkDisplayName = homeworkDisplayName;
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
        this.homeworkCount = 0;
        this.homeworkDisplayName = context.getString(R.string.dailyHomework);
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

    public boolean canIncrementLittleHouse() {
        boolean isCheckPass = true;

        for (String littleHouseMantra: this.littleHouse.getLittleHouseMap().keySet()) {
            // iterate through the littleHouse map -> Now looking at it...its useless rip💀 😣😖😫😩😭
            if (this.littleHouse.getLittleHouseMap().get(littleHouseMantra) < 1.0) {
                isCheckPass = false;
                break;
            }
        }

        return isCheckPass;
    }

    public boolean incrementLittleHouse() {
        boolean isCheckPass = true;

        //checking if the relevant counters are correct
        for (String littleHouseMantra: this.littleHouse.getLittleHouseMap().keySet()) {
            // iterate through the littleHouse map -> Now looking at it...its useless rip💀 😣😖😫😩😭
            if (this.littleHouse.getLittleHouseMap().get(littleHouseMantra) < 1.0) {
                isCheckPass = false;
                break;
            }
        }

        if (isCheckPass) {
            this.littleHouse.setLittleCount(this.littleHouse.getLittleHouseCount() + 1);

            // decrementing all the values inside LittleHouseMap
            for (String littleHouseMantra: this.littleHouse.getLittleHouseMap().keySet()) {
                // iterate through the littleHouse map -> Now looking at it...its useless rip💀 😣😖😫😩😭
                this.littleHouse.getLittleHouseMap().put(littleHouseMantra, this.littleHouse.getLittleHouseMap().get(littleHouseMantra) - 1);
            }

            for (int index = 0; index < counters.size(); index++) {
                if (counters.get(index).getOriginalName().equals(this.counters.get(0).getDabei())) {
                    counters.get(index).setCount(counters.get(index).getCount() - DaBeiLimit);
                }
                else if (counters.get(index).getOriginalName().equals(this.counters.get(0).getBoruo())) {
                    counters.get(index).setCount(counters.get(index).getCount() - BoRuoLimit);
                }
                else if (counters.get(index).getOriginalName().equals(this.counters.get(0).getQifo())) {
                    counters.get(index).setCount(counters.get(index).getCount() - QiFoLimit);
                }
                else if (counters.get(index).getOriginalName().equals(this.counters.get(0).getWangshen())) {
                    counters.get(index).setCount(counters.get(index).getCount() - WangShenLimit);
                }
            }
        }

        return isCheckPass;
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

    public Integer getHomeworkCount() {
        return homeworkCount;
    }

    public void setHomeworkCount(Integer homeworkCount) {
        this.homeworkCount = homeworkCount;
    }

    public String getHomeworkDisplayName() {
        return homeworkDisplayName;
    }

    public void setHomeworkDisplayName(String homeworkDisplayName) {
        this.homeworkDisplayName = homeworkDisplayName;
    }

    /**
     * assume homework can be completed
     */
    public void incrementHomework() {

        for (Counter counter: counters) {
            for (int i =0; i < counter.getHomeworkAmount(); i++) {
                this.decrement(counter.getOriginalName());
            }
        }
        homeworkCount++;
    }

    /**
     * Checks if homework can be completed
     * @return true if homework can be completed, false otherwise
     */
    public boolean canCompleteHomework() {
        for (Counter counter: counters) {
            if (counter.homeworkAmountMissing() != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean decrementHomework() {
        if (homeworkCount - 1 < 0) {
            return false;
        }
        homeworkCount--;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setNewHomeworkTimeDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("'Date:\n'MM-dd-yyyy '\n\nTime:\n'hh:mm a");
        lastHomeworkDateTime = sdf.format(new Date());
    }

    public void resetHomeworkCount() {
        homeworkCount = 0;
    }
}
