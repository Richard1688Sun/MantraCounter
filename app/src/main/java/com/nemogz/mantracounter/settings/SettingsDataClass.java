package com.nemogz.mantracounter.settings;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class SettingsDataClass {

    @ColumnInfo
    private boolean homeSelectTrash;

    @ColumnInfo
    private int numberSettingsItem;

    @ColumnInfo
    private boolean autoCalLittleHouse;

    @ColumnInfo
    private boolean addSubButtonMode;

    @PrimaryKey
    @NotNull
    public String id = "settingsDataClass";

    @Ignore
    public SettingsDataClass(boolean homeSelectTrash) {
        this.homeSelectTrash = homeSelectTrash;
        this.numberSettingsItem = 2;
        this.autoCalLittleHouse = false;
        this.addSubButtonMode = false;
    }

    public SettingsDataClass(boolean homeSelectTrash, int numberSettingsItem, boolean autoCalLittleHouse, boolean addSubButtonMode) {
        this.homeSelectTrash = homeSelectTrash;
        this.numberSettingsItem = numberSettingsItem;
        this.autoCalLittleHouse = autoCalLittleHouse;
        this.addSubButtonMode = addSubButtonMode;
    }

    public boolean isHomeSelectTrash() {
        return homeSelectTrash;
    }

    public void setHomeSelectTrash(boolean homeSelectTrash) {
        this.homeSelectTrash = homeSelectTrash;
    }

    public int getNumberSettingsItem() {
        return numberSettingsItem;
    }

    public void setNumberSettingsItem(int numberSettingsItem) {
        this.numberSettingsItem = numberSettingsItem;
    }

    public boolean isAutoCalLittleHouse() {
        return autoCalLittleHouse;
    }

    public void setAutoCalLittleHouse(boolean autoCalLittleHouse) {
        this.autoCalLittleHouse = autoCalLittleHouse;
    }

    public boolean isAddSubButtonMode() {
        return addSubButtonMode;
    }

    public void setAddSubButtonMode(boolean addSubButtonMode) {
        this.addSubButtonMode = addSubButtonMode;
    }
}
