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
    @Ignore
    private static final int DaBeiLimit = 27;
    @Ignore
    private static final int BoRuoLimit = 49;
    @Ignore
    private static final int WangShenLimit = 84;
    @Ignore
    private static final int QiFoLimit = 87;
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
    private final String name;
    @ColumnInfo
    private Integer count;

    public Counter(@NonNull String name, Integer count, String dabei, String boruo, String qifo, String wangshen) {
        this.name = name;
        this.count = count;
        this.dabei = dabei;
        this.boruo = boruo;
        this.wangshen = wangshen;
        this.qifo = qifo;
    }

    public Counter(@NonNull String name, Integer count, Context context) {
        this.name = name;
        this.count = count;
        this.context = context;
        this.dabei = context.getString(R.string.dabei);
        this.boruo = context.getString(R.string.boruo);
        this.wangshen = context.getString(R.string.wangshen);
        this.qifo = context.getString(R.string.qifo);
    }

    @Override
    public boolean increment(){
        count++;

        //check if each mantra count is satisfied
        if((name.equals(dabei)) && (count >= DaBeiLimit)){
            count = count - DaBeiLimit;
            return true;
        }
        else if(name.equals(boruo) && count >= BoRuoLimit){
            count = count - BoRuoLimit;
            return true;
        }
        else if(name.equals(wangshen) && count >= WangShenLimit){
            count = count - WangShenLimit;
            return true;
        }
        else if(name.equals(qifo) && count >= QiFoLimit){
            count = count - QiFoLimit;
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

    public String getName() {
        return name;
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
}
