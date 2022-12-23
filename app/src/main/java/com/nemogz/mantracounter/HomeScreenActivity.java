package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.homescreen.CounterMainRecViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    private RecyclerView homeRecView;
    public MasterCounterDatabase db;
    private MasterCounter masterCounter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        createDataBase(getApplicationContext());
        masterCounter = new MasterCounter(getApplicationContext());
        if (!loadDataFromDatabase()) {
            createEssentailCounters();
        }

        homeRecView = findViewById(R.id.HomeScreenRecView);

        CounterMainRecViewAdapter counterAdapter = new CounterMainRecViewAdapter(this);
        counterAdapter.setCounters(masterCounter.getCounters());

        homeRecView.setAdapter(counterAdapter);
        //means the layout is vertical
        homeRecView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void createEssentailCounters(){
        masterCounter = new MasterCounter(getApplicationContext());
        masterCounter.createBasicCounters();
    }

    private void createDataBase(Context context) {
        db = MasterCounterDatabase.getINSTANCE(getApplicationContext());
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
        masterCounter.setCounters(c);
        masterCounter.setLittleHouse(lh);
        return true;
    }
}