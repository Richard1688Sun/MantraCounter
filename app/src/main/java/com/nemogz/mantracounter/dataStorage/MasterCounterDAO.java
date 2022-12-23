package com.nemogz.mantracounter.dataStorage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;

import java.util.List;

@Dao
public interface MasterCounterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllCounters(List<Counter> counters);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertLittleHouse(LittleHouse littleHouse);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public void insertCounterPosition(int position);

    @Query("SELECT * FROM counter")
    public List<Counter> getAllCounters();

    @Query("SELECT * FROM littlehouse")
    public LittleHouse getLittleHouse();
}
