package com.nemogz.mantracounter.dataStorage;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;

@Database(entities = {Counter.class, LittleHouse.class}, version = 1, exportSchema = false)
@TypeConverters(Convertors.class)
public abstract class MasterCounterDatabase extends RoomDatabase {
    public abstract MasterCounterDAO masterCounterDAO();
}
