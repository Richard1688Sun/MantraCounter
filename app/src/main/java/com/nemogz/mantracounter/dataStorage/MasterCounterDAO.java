package com.nemogz.mantracounter.dataStorage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;

import java.util.List;

@Dao
public interface MasterCounterDAO {

    @Insert
    public void insertAllCounters(List<Counter> counters);

    @Insert
    public void insertLittleHouse(LittleHouse littleHouse);

    @Query("SELECT * FROM counter")
    public List<Counter> getAllCounters();

    @Query("SELECT * FROM littlehouse")
    public LittleHouse getLittleHouse();
}
