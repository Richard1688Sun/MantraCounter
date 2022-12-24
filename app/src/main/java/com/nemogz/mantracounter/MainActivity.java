package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.settings.SettingsDataClass;

public class MainActivity extends AppCompatActivity {

    //to access the strings in classes
    private static Resources resources;
    public MasterCounterDatabase db;

    private Button buttonCounter;
    private FloatingActionButton buttonHome;
    private FloatingActionButton buttonMode;
    private FloatingActionButton buttonTool;
    private FloatingActionButton buttonLeft;
    private FloatingActionButton buttonRight;
    private TextView textMantra;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private TextView t5;

    private Boolean addMode = true;
    private MasterCounter masterCounter;
//    private Vibrator vibrator;
//    private boolean vibrate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("state", "onCreate");
        resources = getResources();
        masterCounter = new MasterCounter(getApplicationContext());
        setContentView(R.layout.counter_screen);
        createDataBase(getApplicationContext());
        inputInitialSettings();
        if (!loadDataFromDatabase()) {
            createEssentailCounters();
        }
        instantiateViews();
        setCounterView();

//        if(vibrator.hasVibrator()){
//            vibrate = true;
//        }

        //detect short press
        buttonCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addMode){
                    masterCounter.increment(masterCounter.getCounterAtPosition().getName());
                }else{
                    masterCounter.decrement(masterCounter.getCounterAtPosition().getName());;
                }
                setCounterView();
            }
        });

        //detect long press
        buttonCounter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                masterCounter.setCount(masterCounter.getCounterAtPosition().getName(), 0);
                setCounterView();
                return true;
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                masterCounter.incrementPositionCounter();
                setCounterView();
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                masterCounter.decrementPositionCounter();
                setCounterView();
            }
        });

        buttonMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addMode) {
                    addMode = false;
                    buttonMode.setImageResource(R.drawable.ic_add_sign);
                }
                else{
                    addMode = true;
                    buttonMode.setImageResource(R.drawable.ic_sub_sign);
                }
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeScreen();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("state", "onPause");
        setDataFromDatabase();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("state", "onRestart");
        loadDataFromDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("state", "onResume");
        loadDataFromDatabase();
    }

    public static Resources getAppResources() {
        return resources;
    }

    public void createEssentailCounters(){
        masterCounter = new MasterCounter(getApplicationContext());
        masterCounter.createBasicCounters();
    }

    private void instantiateViews(){
        buttonCounter = findViewById(R.id.MainButton);
        buttonHome = findViewById(R.id.HomePageButton);
        buttonLeft = findViewById(R.id.LeftButton);
        buttonRight = findViewById(R.id.RightButton);
        buttonMode = findViewById(R.id.ModeButton);
        buttonTool = findViewById(R.id.ToolBarButton);
        textMantra = findViewById(R.id.MantraNameText);
        t1 = findViewById(R.id.textView);
        t2 = findViewById(R.id.textView2);
        t3 = findViewById(R.id.textView3);
        t4 = findViewById(R.id.textView4);
        t5 = findViewById(R.id.textView5);
    }

    private void setCounterView(){
        textMantra.setText(masterCounter.getCounterAtPosition().getName());
        buttonCounter.setText(masterCounter.getCounterAtPosition().getCount().toString());
        testviewUP();
    }


    private void testviewUP(){
        t1.setText("DaBei = " + Double.toString(masterCounter.getLittleHouse().getCountByName(getString(R.string.dabei))));
        t2.setText("BoRuo = " + Double.toString(masterCounter.getLittleHouse().getCountByName(getString(R.string.boruo))));
        t3.setText("XiaoZai = " + Double.toString(masterCounter.getLittleHouse().getCountByName(getString(R.string.wangshen))));
        t4.setText( "QiFo = " + Double.toString(masterCounter.getLittleHouse().getCountByName(getString(R.string.qifo))));
        t5.setText("XiaoFangZi = " + Integer.toString(masterCounter.getLittleHouse().getLittleHouseCount()));
    }

    private void createDataBase(Context context) {
        db = MasterCounterDatabase.getINSTANCE(getApplicationContext());
    }

    /**
     * Tries to load data from database. Return if successful
     * @return true if data was detected and loaded, false otherwise
     */
    private boolean loadDataFromDatabase() {
        if(db.masterCounterDAO().getLittleHouse() == null) {
            return false;
        }
        masterCounter.setCounters(db.masterCounterDAO().getAllCounters());
        masterCounter.setLittleHouse(db.masterCounterDAO().getLittleHouse());
        masterCounter.setPositionCounters(db.masterCounterDAO().getMasterCounterPosition().getPositionCounters());
        return true;
    }

    private void setDataFromDatabase() {
        db.masterCounterDAO().insertAllCounters(masterCounter.getCounters());
        db.masterCounterDAO().insertLittleHouse(masterCounter.getLittleHouse());
        db.masterCounterDAO().insertCounterPosition(masterCounter);
    }

    private void openHomeScreen() {
        Intent homeScreenIntent = new Intent(this, HomeScreenActivity.class);
        startActivity(homeScreenIntent);
    }

    private void inputInitialSettings() {
        if (db.masterCounterDAO().getSettingsData() == null) {
            db.masterCounterDAO().insertSettingsData(new SettingsDataClass(false));
        }
    }
}