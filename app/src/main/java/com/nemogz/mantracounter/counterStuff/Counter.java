package com.nemogz.mantracounter.counterStuff;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.nemogz.mantracounter.MainActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Interfaces.CounterInterface;

import org.jetbrains.annotations.NotNull;

@Entity
public class Counter implements CounterInterface {

    @Ignore
    Context context;
//    @Ignore
//    private static final int DaBeiLimit = 27;
//    @Ignore
//    private static final int BoRuoLimit = 49;
//    @Ignore
//    private static final int WangShenLimit = 84;
//    @Ignore
//    private static final int QiFoLimit = 87;
    @Ignore
    private static final int DaBeiLimit = 10;
    @Ignore
    private static final int BoRuoLimit = 9;
    @Ignore
    private static final int WangShenLimit = 8;
    @Ignore
    private static final int QiFoLimit = 7;
    @ColumnInfo
    private final String dabei;
    @ColumnInfo
    private final String boruo;
    @ColumnInfo
    private final String wangshen;
    @ColumnInfo
    private final String qifo;


    @PrimaryKey
    @NotNull
    private final String originalName;

    @ColumnInfo
    private String displayName;

    @ColumnInfo
    private Integer count;

    public Counter(@NonNull String originalName, Integer count, String dabei, String boruo, String qifo, String wangshen) {
        this.originalName = originalName;
        this.count = count;
        this.dabei = dabei;
        this.boruo = boruo;
        this.wangshen = wangshen;
        this.qifo = qifo;
        this.displayName = originalName;
    }

    public Counter(@NonNull String originalName, Integer count, Context context) {
        this.originalName = originalName;
        this.displayName = originalName;
        this.count = count;
        this.context = context;
        this.dabei = context.getString(R.string.dabei);
        this.boruo = context.getString(R.string.boruo);
        this.wangshen = context.getString(R.string.wangshen);
        this.qifo = context.getString(R.string.qifo);
    }

    @Override
    public boolean increment(int completedAmount){
        count++;

        //check if each mantra count is satisfied
        if((originalName.equals(dabei)) && (count >= DaBeiLimit * (completedAmount + 1))){
            return true;
        }
        else if(originalName.equals(boruo) && count >= BoRuoLimit * (completedAmount + 1)){
            return true;
        }
        else if(originalName.equals(wangshen) && count >= WangShenLimit * (completedAmount + 1)){
            return true;
        }
        else if(originalName.equals(qifo) && count >= QiFoLimit * (completedAmount + 1)){
            return true;
        }

        return false;
    }

    @Override
    public boolean setCount(int newCount){
        if(newCount >= 0) {
            count = newCount;
            return true;
        }
        return false;
    }

    @Override
    public boolean decrement(){
        if(count - 1 >= 0){
            count--;
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCounter(int timesToUpdate) {
        for(int i = 0; i < timesToUpdate; i++) {
            //check if each mantra count is satisfied
            if((originalName.equals(dabei)) && (count - DaBeiLimit >= 0)){
                count = count - DaBeiLimit;
            }
            else if(originalName.equals(boruo) && (count  - BoRuoLimit >= 0)){
                count = count - BoRuoLimit;
            }
            else if(originalName.equals(wangshen) && (count - WangShenLimit >= 0)){
                count = count - WangShenLimit;
            }
            else if(originalName.equals(qifo) && (count - QiFoLimit >= 0)){
                count = count - QiFoLimit;
            }
            else if(!originalName.equals(dabei) && !originalName.equals(boruo) && !originalName.equals(wangshen) && !originalName.equals(qifo)) {
                return false;
            }
        }
        return true;
    }

//    *
//     * finds the number of times this counter has passed the littleHouse limit
//     * @return number of times the limit has been passed
    public int getNumberOfCompletes() {
        if((originalName.equals(dabei)) && (count - DaBeiLimit >= 0)){
            return count/DaBeiLimit;
        }
        else if(originalName.equals(boruo) && (count  - BoRuoLimit >= 0)){
            return count/BoRuoLimit;
        }
        else if(originalName.equals(wangshen) && (count - WangShenLimit >= 0)){
            return count/WangShenLimit;
        }
        else if(originalName.equals(qifo) && (count - QiFoLimit >= 0)){
            return count/QiFoLimit;
        }
        return 0;
    }

    public String getOriginalName() {
        return originalName;
    }

    public Integer getCount() {
        return count;
    }

    public String getDabei() {
        return dabei;
    }

    public String getBoruo() {
        return boruo;
    }

    public String getWangshen() {
        return wangshen;
    }

    public String getQifo() {
        return qifo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
