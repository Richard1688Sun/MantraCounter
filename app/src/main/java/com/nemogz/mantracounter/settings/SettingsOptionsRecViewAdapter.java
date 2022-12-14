package com.nemogz.mantracounter.settings;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
    private SoundPool soundPool;
    private boolean loaded = false;
    private int littleHouseID;
    private Vibrator vibrator;
    boolean hasVibratorFunction;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public SettingsOptionsRecViewAdapter(Context context) {
        this.context = context;
        db = MasterCounterDatabase.getINSTANCE(context);
        masterCounter = new MasterCounter(context);
        createMediaPlayer();
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        hasVibratorFunction = vibrator.hasVibrator();
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
                holder.switchView.setText(context.getString(R.string.settingsAddSub));
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
                holder.switchView.setText(context.getString(R.string.settingsAutoCal));
                holder.switchView.setChecked(settingsDataClass.isAutoCalLittleHouse());
                holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settingsDataClass.setAutoCalLittleHouse(isChecked);
                        db.masterCounterDAO().insertSettingsData(settingsDataClass);

                        if (isChecked) {
                            int littleHouseCompleted = masterCounter.getLittleHouse().updateLittleHouseMapAndCount();

                            if (littleHouseCompleted != 0) {
                                Toast.makeText(context, context.getString(R.string.Completed)+" " + littleHouseCompleted + " " + context.getString(R.string.xiaofangzi), Toast.LENGTH_SHORT).show();
                                if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(1000);
                                if (settingsDataClass.isSoundEffect() && loaded) soundPool.play(littleHouseID, 1, 1, 1, 0, 0);
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
                holder.switchView.setText(context.getString(R.string.settingsArrowNavigation));
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
                holder.switchView.setText(context.getString(R.string.settingsSwipeNavigation));
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
                holder.switchView.setText(context.getString(R.string.settingsSideBar));
                holder.switchView.setChecked(settingsDataClass.isSidebarReminder());
                holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settingsDataClass.setSidebarReminder(isChecked);
                        db.masterCounterDAO().insertSettingsData(settingsDataClass);
                    }
                });
                break;
            case 5:
                holder.iconView.setImageResource(R.drawable.ic_sound_icon_foreground);
                holder.switchView.setText(context.getString(R.string.sound));
                holder.switchView.setChecked(settingsDataClass.isSoundEffect());
                holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settingsDataClass.setSoundEffect(isChecked);
                        db.masterCounterDAO().insertSettingsData(settingsDataClass);
                    }
                });
                break;
            case 6:
                holder.iconView.setImageResource(R.drawable.ic_vibration_icon_foreground);
                holder.switchView.setText(context.getString(R.string.vibrations));
                holder.switchView.setChecked(settingsDataClass.isVibrationsEffect());
                holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settingsDataClass.setVibrationsEffect(isChecked);
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

    private void createMediaPlayer() {
        ((Activity)context).getWindow().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Load the sound
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        littleHouseID = soundPool.load(context, R.raw.littlehouse, 1);
    }
}

