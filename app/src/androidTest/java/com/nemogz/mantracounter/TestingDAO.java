package com.nemogz.mantracounter;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Ignore;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDAO;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TestingDAO {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    MasterCounterDatabase db;
    private final String dabei = appContext.getString(R.string.dabei);
    private final String boruo = appContext.getString(R.string.boruo);
    private final String wangshen = appContext.getString(R.string.wangshen);
    private final String qifo = appContext.getString(R.string.qifo);

    @Before
    public void openDatabase() {
        db = Room.inMemoryDatabaseBuilder(appContext, MasterCounterDatabase.class).allowMainThreadQueries().build();
    }

    @After
    public void closeDatabase() {
        db.close();
    }

    @Test
    public void testAddingCounter() {

        Counter c1 = new Counter(dabei, 0, appContext);
        Counter c2 = new Counter("last", 2, appContext);
        Counter c3 = new Counter("not last", 3, appContext);
        List<Counter> list = new ArrayList<>();
        list.add(c1);
        list.add(c2);
        list.add(c3);

        db.masterCounterDAO().insertAllCounters(list);

        List<Counter> returnL = db.masterCounterDAO().getAllCounters();

        for(int i = 0; i < 27; i++){
            returnL.get(0).increment(0);
        }

        Log.d("counterNum", returnL.get(0).getCount().toString());

        for(Counter c: returnL) {
            Log.d("counterName", c.getName());
        }
    }

    @Test
    public void testLittleHouse() {

        LittleHouse lt = new LittleHouse(appContext);
        lt.incrementCount(dabei);
        lt.incrementCount(boruo);
        lt.incrementCount(qifo);
        lt.incrementCount(wangshen);

        db.masterCounterDAO().insertLittleHouse(lt);

        LittleHouse returnLH = db.masterCounterDAO().getLittleHouse();

        for(String man: returnLH.getLittleHouseMap().keySet()) {
            Log.d("LittleHouse", man + " " + returnLH.getLittleHouseMap().get(man));
        }
    }

    @Test
    public void testMasterCounter() {

        MasterCounter masterCounter = new MasterCounter(appContext);
        masterCounter.createBasicCounters();
        masterCounter.increment(dabei);
        masterCounter.increment(boruo);
        masterCounter.increment(wangshen);
        masterCounter.increment(qifo);
        masterCounter.increment(dabei);
        masterCounter.increment(boruo);
        masterCounter.increment(wangshen);
        masterCounter.increment(qifo);
        masterCounter.getLittleHouse().incrementCount(dabei);
        masterCounter.getLittleHouse().incrementCount(boruo);
        masterCounter.getLittleHouse().incrementCount(wangshen);
        masterCounter.getLittleHouse().incrementCount(qifo);

        db.masterCounterDAO().insertLittleHouse(masterCounter.getLittleHouse());
        db.masterCounterDAO().insertAllCounters(masterCounter.getCounters());

        MasterCounter MC2 = new MasterCounter(appContext, db.masterCounterDAO().getLittleHouse(), db.masterCounterDAO().getAllCounters(), 0);

        for(Counter c: MC2.getCounters()) {
            Log.d("MasterCounter", c.getName() + " " + c.getCount());
        }

        for(String man: MC2.getLittleHouse().getLittleHouseMap().keySet()) {
            Log.d("MasterCounter", man + " " + MC2.getLittleHouse().getLittleHouseMap().get(man));
        }
    }
}
