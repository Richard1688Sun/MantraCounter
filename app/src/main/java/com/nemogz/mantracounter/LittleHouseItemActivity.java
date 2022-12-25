package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.settings.SettingsDataClass;

public class LittleHouseItemActivity extends AppCompatActivity {

    public MasterCounterDatabase db;
    private EditText littleHouseNameScreen;
    private Button littleHouseCountScreen;
    private FloatingActionButton buttonHome;
    private FloatingActionButton buttonMode;
    private FloatingActionButton buttonTool;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private TextView t5;
    private float TIME_FOR_LONG_CLICK = 500;

    private Boolean addMode = true;
    private MasterCounter masterCounter;
    private Vibrator vibrator;
    private boolean hasVibratorFunction;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("state", "onCreate");
        masterCounter = new MasterCounter(getApplicationContext());
        setContentView(R.layout.activity_littlehouse_item_screen);
        createDataBase(getApplicationContext());
        inputInitialSettings();
        if (!loadDataFromDatabase()) {
            createEssentailCounters();
        }
        instantiateViews();
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        hasVibratorFunction = vibrator.hasVibrator();
        setCounterView();


        //for all types of clicks on Main Button
        littleHouseCountScreen.setOnTouchListener(new View.OnTouchListener() {
            float xStart;
            float yStart;
            float xEnd;
            float yEnd;
            float clickedDownTime;
            float releasedTime;
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xStart = event.getX();
                        yStart = event.getY();
                        clickedDownTime = event.getEventTime();
                        break;
                    case MotionEvent.ACTION_UP:
                        xEnd = event.getX();
                        yEnd = event.getY();
                        releasedTime = event.getEventTime();
                        if (releasedTime - clickedDownTime > TIME_FOR_LONG_CLICK) {
                            ///long click
                            masterCounter.getLittleHouse().setLittleHouseCount(0);
                            if (hasVibratorFunction) vibrator.vibrate(200);
                            setCounterView();
                        }
                        else {
                            //click
                            if(addMode){
                                masterCounter.getLittleHouse().setLittleHouseCount(masterCounter.getLittleHouse().getLittleHouseCount() + 1);
                            }else{
                                masterCounter.getLittleHouse().decrementLittleHouseCount();
                            }
                            if (hasVibratorFunction) vibrator.vibrate(200);
                            setCounterView();
                        }
                        break;
                }
                return false;
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

        littleHouseNameScreen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("test", "beforeTextChange");
//                switch(masterCounter.getPositionCounters()) {
//                    case 0:
//                        textMantra.setHint(R.string.dabei);
//                        break;
//                    case 1:
//                        textMantra.setHint(R.string.boruo);
//                        break;
//                    case 2:
//                        textMantra.setHint(R.string.wangshen);
//                        break;
//                    case 3:
//                        textMantra.setHint(R.string.qifo);
//                        break;
//                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("test", "duringTextChange");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("test", "afterTextChange " + littleHouseNameScreen.getText());
                masterCounter.getLittleHouse().setLittleHouseDisplayName(littleHouseNameScreen.getText().toString());
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

    public void createEssentailCounters(){
        masterCounter = new MasterCounter(getApplicationContext());
        masterCounter.createBasicCounters();
    }

    private void instantiateViews(){
        littleHouseCountScreen = findViewById(R.id.littleHouseCountScreen);
        buttonHome = findViewById(R.id.HomePageButton);
        buttonMode = findViewById(R.id.ModeButton);
        buttonTool = findViewById(R.id.ToolBarButton);
        littleHouseNameScreen = findViewById(R.id.littleHouseNameScreen);
        t1 = findViewById(R.id.textView);
        t2 = findViewById(R.id.textView2);
        t3 = findViewById(R.id.textView3);
        t4 = findViewById(R.id.textView4);
        t5 = findViewById(R.id.textView5);
    }

    private void setCounterView(){
        littleHouseNameScreen.setText(masterCounter.getLittleHouse().getLittleHouseDisplayName());
        littleHouseCountScreen.setText(masterCounter.getLittleHouse().getLittleHouseCount().toString());
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