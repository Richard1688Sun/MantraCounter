package com.nemogz.mantracounter.dataStorage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.settings.SettingsDataClass;

import java.util.List;

@Dao
public interface MasterCounterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllCounters(List<Counter> counters);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertLittleHouse(LittleHouse littleHouse);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMasterCounter(MasterCounter masterCounter);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSettingsData(SettingsDataClass settingsDataClass);

    @Delete
    public void deleteCounter(Counter counter);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCounter(Counter counter);

    @Update
    public void updateCounter(Counter counter);

    @Query("SELECT * FROM mastercounter")
    public MasterCounter getMasterCounter();

    @Query("SELECT * FROM counter")
    public List<Counter> getAllCounters();

    @Query("SELECT * FROM littlehouse")
    public LittleHouse getLittleHouse();

    @Query("SELECT * FROM settingsdataclass")
    public SettingsDataClass getSettingsData();
}
