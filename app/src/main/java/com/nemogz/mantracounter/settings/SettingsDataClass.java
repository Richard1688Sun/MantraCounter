package com.nemogz.mantracounter.settings;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class SettingsDataClass {

    @ColumnInfo
    private boolean homeSelectTrash;

    @ColumnInfo
    private int numberSettingsItem = 5;

    @ColumnInfo
    private boolean autoCalLittleHouse;

    @ColumnInfo
    private boolean addSubButtonMode;

    @ColumnInfo
    private boolean arrowsNavigation;

    @ColumnInfo
    private boolean swipeNavigation;

    @ColumnInfo
    private boolean sidebarReminder;

    @PrimaryKey
    @NotNull
    public String id = "settingsDataClass";

    public SettingsDataClass(boolean homeSelectTrash, boolean autoCalLittleHouse, boolean addSubButtonMode, boolean arrowsNavigation, boolean swipeNavigation, boolean sidebarReminder) {
        this.homeSelectTrash = homeSelectTrash;
        this.autoCalLittleHouse = autoCalLittleHouse;
        this.addSubButtonMode = addSubButtonMode;
        this.arrowsNavigation = arrowsNavigation;
        this.swipeNavigation = swipeNavigation;
        this.sidebarReminder = sidebarReminder;
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

    public boolean isArrowsNavigation() {
        return arrowsNavigation;
    }

    public void setArrowsNavigation(boolean arrowsNavigation) {
        this.arrowsNavigation = arrowsNavigation;
    }

    public boolean isSwipeNavigation() {
        return swipeNavigation;
    }

    public void setSwipeNavigation(boolean swipeNavigation) {
        this.swipeNavigation = swipeNavigation;
    }

    public boolean isSidebarReminder() {
        return sidebarReminder;
    }

    public void setSidebarReminder(boolean sidebarReminder) {
        this.sidebarReminder = sidebarReminder;
    }
}
