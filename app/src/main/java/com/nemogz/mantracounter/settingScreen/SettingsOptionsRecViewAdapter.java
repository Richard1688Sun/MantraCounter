package com.nemogz.mantracounter.settingScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.homescreen.CounterMainRecViewAdapter;
import com.nemogz.mantracounter.settings.SettingsDataClass;

public class SettingsOptionsRecViewAdapter extends RecyclerView.Adapter<SettingsOptionsRecViewAdapter.ViewHolder> {

    private MasterCounter masterCounter = new MasterCounter(0);
    private SettingsDataClass settingsDataClass;
    private Context context;
    public MasterCounterDatabase db;

    public SettingsOptionsRecViewAdapter(Context context) {
        this.context = context;
        db = MasterCounterDatabase.getINSTANCE(context);
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

