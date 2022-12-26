package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.settings.SettingsOptionsRecViewAdapter;
import com.nemogz.mantracounter.settings.SettingsDataClass;

import java.util.List;

public class SettingsScreen extends AppCompatActivity {

    public MasterCounterDatabase db;
    private MasterCounter masterCounter = new MasterCounter(0);
    private RecyclerView settingsRecView;
    private FloatingActionButton homeButton;
    private SettingsDataClass settingsDataClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        createDataBase(getApplicationContext());

        if (!loadDataFromDatabase()) {
            createEssentailCounters();
        }
        instantiateViews();

        SettingsOptionsRecViewAdapter settingsOptionsRecViewAdapter = new SettingsOptionsRecViewAdapter(this);
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
    }

    public void createEssentailCounters(){
        masterCounter = new MasterCounter(getApplicationContext());
        masterCounter.createBasicCounters();
    }

    /**
     * Tries to load data from database. Return if successful
     * @return true if data was detected and loaded, false otherwise
     */
    //TODO maybe not necessary to check since this is not starting screen
    private boolean loadDataFromDatabase() {
        if(db.masterCounterDAO().getAllCounters().size() == 0 && db.masterCounterDAO().getLittleHouse() == null) {
            return false;
        }
        List<Counter> c = db.masterCounterDAO().getAllCounters();
        LittleHouse lh = db.masterCounterDAO().getLittleHouse();
        masterCounter.setCounters(c);
        masterCounter.setLittleHouse(lh);
        masterCounter.setPositionCounters(db.masterCounterDAO().getMasterCounterPosition().getPositionCounters());
        settingsDataClass = db.masterCounterDAO().getSettingsData();
        return true;
    }

    private void createDataBase(Context context) {
        db = MasterCounterDatabase.getINSTANCE(context);
    }

    private void instantiateViews() {
        settingsRecView = findViewById(R.id.settingsRecView);
        homeButton = findViewById(R.id.HomePageButton);
    }
}