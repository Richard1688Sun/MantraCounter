package com.nemogz.mantracounter.dataStorage;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nemogz.mantracounter.counterStuff.LittleHouse;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Convertors {

    private final Gson gson = new Gson();

    @TypeConverter
    public String littleHouseToGson(Map<String, Integer> littleHouse) {
        return littleHouse == null ? null: gson.toJson(littleHouse);
    }

    @TypeConverter
    public Map<String, Integer> gsonToLittleHouse(String litteHouse) {
        return litteHouse == null ? null :gson.fromJson(litteHouse, Map.class);
    }
}
