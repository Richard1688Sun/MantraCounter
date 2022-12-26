package com.nemogz.mantracounter.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;

public class SettingsOptionsRecViewAdapter extends RecyclerView.Adapter<SettingsOptionsRecViewAdapter.ViewHolder> {

    private MasterCounter masterCounter;
    private SettingsDataClass settingsDataClass;
    private Context context;
    public MasterCounterDatabase db;

    public SettingsOptionsRecViewAdapter(Context context) {
        this.context = context;
        db = MasterCounterDatabase.getINSTANCE(context);
        masterCounter = new MasterCounter(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_options_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return db.masterCounterDAO().getSettingsData().getNumberSettingsItem();
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsOptionsRecViewAdapter.ViewHolder holder, int position) {

        switch (position) {
            case 0:
                holder.iconView.setImageResource(R.drawable.ic_touch_icon_foreground);
                holder.switchView.setText("Enable Add and Aubtract Mode");
                holder.switchView.setChecked(settingsDataClass.isAddSubButtonMode());
                holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settingsDataClass.setAddSubButtonMode(isChecked);
                        db.masterCounterDAO().insertSettingsData(settingsDataClass);
                    }
                });
                break;
            case 1:
                holder.iconView.setImageResource(R.drawable.ic_auto_cal_littlehouse_foreground);
                holder.switchView.setText("Enable Auto LittleHouse Calculation");
                holder.switchView.setChecked(settingsDataClass.isAutoCalLittleHouse());
                holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settingsDataClass.setAutoCalLittleHouse(isChecked);
                        db.masterCounterDAO().insertSettingsData(settingsDataClass);

                        if (isChecked) {
                            //finding the # of littleHouse completed
//                            for (Counter counter: masterCounter.getCounters()) {
//                                if (masterCounter.getLittleHouse().getLittleHouseMap().containsKey(counter.getOriginalName())) {
//                                    int counterCompletes = counter.getNumberOfCompletes();
//                                    masterCounter.getLittleHouse().incrementByValueCount(counter.getOriginalName(), counterCompletes);
//                                }
//                            }
                            int littleHouseCompleted = masterCounter.getLittleHouse().updateLittleHouseMapAndCount();

                            if (littleHouseCompleted != 0) {
                                Toast.makeText(context, "Completed " + littleHouseCompleted + " " + context.getString(R.string.xiaofangzi), Toast.LENGTH_SHORT).show();
                                for (Counter counter: masterCounter.getCounters()) {
                                    if (masterCounter.getLittleHouse().getLittleHouseMap().containsKey(counter.getOriginalName())) {
                                        counter.updateCounter(littleHouseCompleted);
                                    }
                                }
                                db.masterCounterDAO().insertAllCounters(masterCounter.getCounters());
                            }
                            db.masterCounterDAO().insertLittleHouse(masterCounter.getLittleHouse());
                        }
                    }
                });
                break;
            case 2:
                holder.iconView.setImageResource(R.drawable.ic_arrows_top_allow_foreground);
                holder.switchView.setText("Enable Arrow Navigiation");
                holder.switchView.setChecked(settingsDataClass.isArrowsNavigation());
                holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settingsDataClass.setArrowsNavigation(isChecked);
                        db.masterCounterDAO().insertSettingsData(settingsDataClass);
                    }
                });
                break;
            case 3:
                holder.iconView.setImageResource(R.drawable.ic_swipe_allow_foreground);
                holder.switchView.setText("Enable Swipe Navigiation");
                holder.switchView.setChecked(settingsDataClass.isSwipeNavigation());
                holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settingsDataClass.setSwipeNavigation(isChecked);
                        db.masterCounterDAO().insertSettingsData(settingsDataClass);
                    }
                });
                break;
            case 4:
                holder.iconView.setImageResource(R.drawable.ic_sidebar_allow_foreground);
                holder.switchView.setText("Enable Sidebar Reminder");
                holder.switchView.setChecked(settingsDataClass.isSidebarReminder());
                holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settingsDataClass.setSidebarReminder(isChecked);
                        db.masterCounterDAO().insertSettingsData(settingsDataClass);
                    }
                });
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iconView;
        Switch switchView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.settingsItemImageView);
            switchView = itemView.findViewById(R.id.settingsItemSwitchView);
        }
    }

    public void setMasterCounter(MasterCounter masterCounter) {
        this.masterCounter = masterCounter;
        notifyDataSetChanged();
    }

    public void setSettingsDataClass(SettingsDataClass settingsDataClass) {
        this.settingsDataClass = settingsDataClass;
        notifyDataSetChanged();
    }
}

