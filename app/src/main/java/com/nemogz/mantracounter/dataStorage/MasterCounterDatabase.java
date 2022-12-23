package com.nemogz.mantracounter.dataStorage;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;

@Database(entities = {Counter.class, LittleHouse.class}, version = 2)
@TypeConverters(Convertors.class)
public abstract class MasterCounterDatabase extends RoomDatabase {

    private static MasterCounterDatabase INSTANCE;
    public abstract MasterCounterDAO masterCounterDAO();

    public static MasterCounterDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MasterCounterDatabase.class, "MasterCounterDB").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        if (INSTANCE != null) INSTANCE.close();
        INSTANCE = null;
    }
}
