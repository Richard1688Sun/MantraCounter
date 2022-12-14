package com.nemogz.mantracounter.homescreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.nemogz.mantracounter.HomeScreenActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.settings.SettingsDataClass;

import java.util.ArrayList;
import java.util.List;

public class ChangeCounterPrompt extends AppCompatDialogFragment {

    private EditText mantraName;
    private EditText mantraCount;
    public MasterCounterDatabase db;
    private List<Counter> counters;
    private LittleHouse littleHouse;
    private int position;
    private CounterMainRecViewAdapter adapter;
    private HomeScreenActivity homeScreenActivity;
    private SettingsDataClass settingsDataClass;
    private Vibrator vibrator;
    private Context context;
    private SoundPool soundPool;
    private boolean loaded = false;
    private int littleHouseID;

    public ChangeCounterPrompt(CounterMainRecViewAdapter adapter, int position, HomeScreenActivity homeScreenActivity, Context context) {
        this.position = position;
        this.adapter = adapter;
        this.homeScreenActivity = homeScreenActivity;
        this.context = context;
        createMediaPlayer();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        createDataBase(getContext());
        counters = db.masterCounterDAO().getAllCounters();
        vibrator = (Vibrator)context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_counter_prompt, null);

        mantraName = view.findViewById(R.id.newMantraName);
        mantraCount = view.findViewById(R.id.newMantraCount);
        mantraName.setHint(getString(R.string.NewName));
        mantraCount.setHint(getString(R.string.NewCount));

        builder.setView(view).setTitle(getString(R.string.ChangeCounter) + ": " + counters.get(position).getDisplayName()).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), getString(R.string.Change) +" " + counters.get(position).getDisplayName() + " " + getString(R.string.Cancelled), Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton(getString(R.string.Change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editable notConvertedName = mantraName.getEditableText();
                Editable unParsedInt = mantraCount.getEditableText();

                String name = notConvertedName.toString().equals("")? counters.get(position).getDisplayName(): notConvertedName.toString();
                int newCount = unParsedInt.toString().equals("")? counters.get(position).getCount(): Integer.parseInt(unParsedInt.toString());

                counters.get(position).setCount(newCount);
                counters.get(position).setDisplayName(name);
                if (db.masterCounterDAO().getLittleHouse().getLittleHouseMap().containsKey(counters.get(position).getOriginalName())) {
                    littleHouse = db.masterCounterDAO().getLittleHouse();
                    int counterCompletes = counters.get(position).getNumberOfCompletes();
                    littleHouse.resetLittleHouseByName(counters.get(position).getOriginalName());
                    littleHouse.incrementByValueCount(counters.get(position).getOriginalName(), counterCompletes);

                    int littleHouseCompleted = littleHouse.findLittleHouseCompleted();

                    settingsDataClass = db.masterCounterDAO().getSettingsData();
                    if(littleHouseCompleted != 0 && settingsDataClass.isAutoCalLittleHouse()) {
                        Toast.makeText(getContext(), getString(R.string.Completed)+" " + littleHouseCompleted + " " + getContext().getString(R.string.xiaofangzi), Toast.LENGTH_SHORT).show();
                        if (settingsDataClass.isSoundEffect() && loaded) soundPool.play(littleHouseID, 1, 1, 1, 0, 0);
                        if(vibrator.hasVibrator() && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(1000);
                        for (Counter counter: counters) {
                            if (littleHouse.getLittleHouseMap().containsKey(counter.getOriginalName())) {
                                counter.updateCounter(littleHouseCompleted);
                            }
                        }
                        littleHouse.updateLittleHouseMapAndCount(littleHouseCompleted);
                        db.masterCounterDAO().insertLittleHouse(littleHouse);
                        db.masterCounterDAO().insertAllCounters(counters);
                        //resets the grid view to include new counter
                        adapter.setCounters(counters);
                        homeScreenActivity.setLittleHouse(littleHouse);
                        homeScreenActivity.setBasicCounterView();
                        adapter.notifyItemRangeChanged(0, 4);
                    }
                    else{
                        db.masterCounterDAO().insertLittleHouse(littleHouse);
                        db.masterCounterDAO().insertAllCounters(counters);
                        //resets the grid view to include new counter
                        adapter.setCounters(counters);
                        adapter.notifyItemChanged(position);
                    }
                }
                else {
                    db.masterCounterDAO().insertAllCounters(counters);
                    //resets the grid view to include new counter
                    adapter.setCounters(counters);
                    adapter.notifyItemChanged(position);
                }
            }
        });

        return builder.create();
    }

    private void createDataBase(Context context) {
        db = MasterCounterDatabase.getINSTANCE(context);
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