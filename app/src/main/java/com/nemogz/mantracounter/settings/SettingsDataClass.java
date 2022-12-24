package com.nemogz.mantracounter.settings;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class SettingsDataClass {

    @ColumnInfo
    private boolean homeSelectTrash;

    @PrimaryKey
    @NotNull
    public String id = "settingsDataClass";

    public SettingsDataClass(boolean homeSelectTrash) {
        this.homeSelectTrash = homeSelectTrash;
    }

    public boolean isHomeSelectTrash() {
        return homeSelectTrash;
    }

    public void setHomeSelectTrash(boolean homeSelectTrash) {
        this.homeSelectTrash = homeSelectTrash;
    }
}
