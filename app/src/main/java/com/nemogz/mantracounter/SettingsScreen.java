package com.nemogz.mantracounter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.settings.ResetPrompt;
import com.nemogz.mantracounter.settings.SettingsOptionsRecViewAdapter;
import com.nemogz.mantracounter.settings.SettingsDataClass;

import java.util.List;

public class SettingsScreen extends AppCompatActivity {

    public MasterCounterDatabase db;
    private MasterCounter masterCounter = new MasterCounter(0,0,"temp");
    private RecyclerView settingsRecView;
    private FloatingActionButton homeButton;
    private SettingsDataClass settingsDataClass;
    private Context context;
    private FloatingActionButton resetButton;
    private SettingsOptionsRecViewAdapter settingsOptionsRecViewAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        createDataBase(getApplicationContext());

        if (!loadDataFromDatabase()) {
            createEssentailCounters();
        }
        instantiateViews();

        settingsOptionsRecViewAdapter = new SettingsOptionsRecViewAdapter(this);
        settingsOptionsRecViewAdapter.setMasterCounter(masterCounter);
        settingsOptionsRecViewAdapter.setSettingsDataClass(settingsDataClass);

        settingsRecView.setAdapter(settingsOptionsRecViewAdapter);

        settingsRecView.setLayoutManager(new LinearLayoutManager(this));

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeScreenIntent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(homeScreenIntent);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPrompt resetPrompt = new ResetPrompt(SettingsScreen.this);
                resetPrompt.show(getSupportFragmentManager(), "test");
            }
        });
    }

    //not needed since data is set inside adapter
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d("state", "onPause");
//        setDataFromDatabase();
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("state", "onRestart");
        loadDataFromDatabase();
        settingsOptionsRecViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("state", "onResume");
        loadDataFromDatabase();
        settingsOptionsRecViewAdapter.notifyDataSetChanged();
    }

    private void setDataFromDatabase() {
        db.masterCounterDAO().insertAllCounters(masterCounter.getCounters());
        db.masterCounterDAO().insertLittleHouse(masterCounter.getLittleHouse());
        db.masterCounterDAO().insertMasterCounter(masterCounter);
        db.masterCounterDAO().insertSettingsData(settingsDataClass);
    }

    public void createEssentailCounters(){
        masterCounter = new MasterCounter(getApplicationContext());
        masterCounter.createBasicCounters();
    }

    /**
     * Tries to load data from database. Return if successful
     * @return true if data was detected and loaded, false otherwise
     */
    private boolean loadDataFromDatabase() {
        if(db.masterCounterDAO().getAllCounters().size() == 0 && db.masterCounterDAO().getLittleHouse() == null) {
            return false;
        }
        List<Counter> c = db.masterCounterDAO().getAllCounters();
        LittleHouse lh = db.masterCounterDAO().getLittleHouse();
        masterCounter = db.masterCounterDAO().getMasterCounter();
        masterCounter.setCounters(c);
        masterCounter.setLittleHouse(lh);
//        masterCounter.setPositionCounters(mc.getPositionCounters());
//        masterCounter.setHomeworkDisplayName(mc.getHomeworkDisplayName());
//        masterCounter.setHomeworkCount(mc.getHomeworkCount());
//        settingsDataClass = db.masterCounterDAO().getSettingsData();
        return true;
    }

    private void createDataBase(Context context) {
        db = MasterCounterDatabase.getINSTANCE(context);
    }

    private void instantiateViews() {
        settingsRecView = findViewById(R.id.settingsRecView);
        homeButton = findViewById(R.id.HomePageButton);
        resetButton = findViewById(R.id.resetButtonSettings);
    }

    public void setSettingsAdapter() {
        loadDataFromDatabase();
        settingsOptionsRecViewAdapter.setMasterCounter(masterCounter);
        settingsOptionsRecViewAdapter.setSettingsDataClass(settingsDataClass);
        settingsRecView.setAdapter(settingsOptionsRecViewAdapter);
    }
}