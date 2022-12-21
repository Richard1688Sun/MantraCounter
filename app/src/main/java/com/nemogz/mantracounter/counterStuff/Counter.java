package com.nemogz.mantracounter.counterStuff;

import com.nemogz.mantracounter.MainActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Interfaces.CounterInterface;

public class Counter implements CounterInterface {

    private static final int DaBeiLimit = 27;
    private static final int BoRuoLimit = 49;
    private static final int WangShenLimit = 84;
    private static final int QiFoLimit = 87;
    private static final String dabei = MainActivity.getAppResources().getString(R.string.dabei);
    private static final String boruo = MainActivity.getAppResources().getString(R.string.boruo);
    private static final String wangshen = MainActivity.getAppResources().getString(R.string.wangshen);
    private static final String xiaozai = MainActivity.getAppResources().getString(R.string.xiaozai);
    private static final String qifo = MainActivity.getAppResources().getString(R.string.qifo);


    private String name;
    private Integer count;

    public Counter(String name, Integer count) {
        this.name = name;
        this.count = count;
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


}
