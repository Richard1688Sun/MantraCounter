package com.nemogz.mantracounter;

import org.junit.Test;

import com.nemogz.mantracounter.counterStuff.Counter;

public class CounterClassTests{

    private static final int DaBeiLimit = 27;
    private static final int BoRuoLimit = 49;
    private static final int XiaoZhaiLimit = 84;
    private static final int QiFoLimit = 87;
//    private static final String dabei = MainActivity.getAppResources().getString(R.string.dabei);
//    private static final String boruo = MainActivity.getAppResources().getString(R.string.boruo);
//    private static final String xiaozai = MainActivity.getAppResources().getString(R.string.xiaozai);
//    private static final String qifo = MainActivity.getAppResources().getString(R.string.qifo);

    @Test
    public void Test1(){
        Counter daBeiC = new Counter(MainActivity.getAppResources().getString(R.string.dabei), 0);
//        Counter boRuoC = new Counter(boruo, 0);
//        Counter xiaoZaiC = new Counter(xiaozai, 0);
//        Counter qiFoC = new Counter(qifo, 0);

        Counter.createLittleHouse();
        incrementTime(DaBeiLimit, daBeiC);
        System.out.println(Counter.getLittleHouse().getClass());
        //assertEquals(1, Counter.getLittleHouse().get(daBeiC));

    }

    /**
     * Mutates the counter that is passed by incrementing n times
     * @param n times to increment
     * @param count the counter that is mutated
     */
    private void incrementTime(int n, Counter count){
        for(int i = 0; i < n; i++){
            count.increment();
        }
    }
}
