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
    public String littleHouseToGson(Map<String, Double> littleHouse) {
        return littleHouse == null ? null: gson.toJson(littleHouse);
    }

    @TypeConverter
    public Map<String, Double> gsonToLittleHouse(String litteHouse) {
        Map<String, Double> map = litteHouse == null ? null :gson.fromJson(litteHouse, Map.class);
        return map;
    }
}
