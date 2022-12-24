package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.homescreen.CounterMainRecViewAdapter;

import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    private RecyclerView mantraCountersListView;
    private CardView littleHouseItemView;
    private TextView littleHouseNameItem;
    private TextView littleHouseCountItem;
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
        instantiateViews();
        setBasicCounterView();

        CounterMainRecViewAdapter counterAdapter = new CounterMainRecViewAdapter(this);
        counterAdapter.setMasterCounters(masterCounter);

        mantraCountersListView.setAdapter(counterAdapter);
        //means the layout is vertical
        mantraCountersListView.setLayoutManager(new GridLayoutManager(this, 2));

        littleHouseItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent littleHouseScreenIntent = new Intent(getApplicationContext(), LittleHouseItemActivity.class);
                startActivity(littleHouseScreenIntent);
            }
        });
    }

    //TODO update the adapter whenever there is a pause or exit from this activity

    public void createEssentailCounters(){
        masterCounter = new MasterCounter(getApplicationContext());
        masterCounter.createBasicCounters();
    }

    private void instantiateViews() {
        mantraCountersListView = findViewById(R.id.HomeScreenRecView);
        littleHouseItemView = findViewById(R.id.littleHouseItem);
        littleHouseCountItem = findViewById(R.id.littleHouseCountItem);
        littleHouseNameItem = findViewById(R.id.littleHouseNameItem);
    }

    private void setBasicCounterView() {
        littleHouseNameItem.setText(R.string.xiaofangzi);
        littleHouseCountItem.setText(masterCounter.getLittleHouse().getLittleHouseCount().toString());
    }

    private void createDataBase(Context context) {
        db = MasterCounterDatabase.getINSTANCE(context);
    }

    private void updateLittleHouseCounts() {

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
        masterCounter.setPositionCounters(db.masterCounterDAO().getMasterCounterPosition().getPositionCounters());
        return true;
    }
}