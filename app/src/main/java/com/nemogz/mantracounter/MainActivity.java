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
import android.widget.Toast;

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
    private EditText textMantra;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private TextView t5;
    private float DISTANCE_FOR_SWIPE = 150;
    private float TIME_FOR_LONG_CLICK = 2000;

    private Boolean addMode = true;
    private MasterCounter masterCounter;
    private SettingsDataClass settingsDataClass;
    private Vibrator vibrator;
    private boolean hasVibratorFunction;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("state", "onCreate");
        resources = getResources();
        masterCounter = new MasterCounter(getApplicationContext());
        setContentView(R.layout.counter_screen);
        createDataBase(getApplicationContext());
        if (!loadDataFromDatabase()) {
            createEssentailCounters();
            inputInitialSettings();
        }
        instantiateViews();
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        hasVibratorFunction = vibrator.hasVibrator();
        setCounterView();


        if (settingsDataClass.isSidebarReminder()) {
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            t3.setVisibility(View.VISIBLE);
            t4.setVisibility(View.VISIBLE);
            t5.setVisibility(View.VISIBLE);
        }
        else {
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
            t3.setVisibility(View.GONE);
            t4.setVisibility(View.GONE);
            t5.setVisibility(View.GONE);
        }

        //for all types of clicks on Main Button
        buttonCounter.setOnTouchListener(new View.OnTouchListener() {
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

                        float xDiff = xStart - xEnd;
                        //TODO track y location maybe
                        float yDiff = yStart - yEnd;

                        if (settingsDataClass.isSwipeNavigation() && Math.abs(xDiff) > DISTANCE_FOR_SWIPE) {
                            if(xDiff < 0) {
                                //left swipe
                                masterCounter.decrementPositionCounter();
                                setCounterView();
                            }
                            else {
                                //right swipe
                                masterCounter.incrementPositionCounter();
                                setCounterView();
                            }
                        }
                        else {
                            if (releasedTime - clickedDownTime > TIME_FOR_LONG_CLICK) {
                                ///long click
                                masterCounter.setCount(masterCounter.getCounterAtPosition().getOriginalName(), 0);
                                if (hasVibratorFunction) vibrator.vibrate(200);
                                setCounterView();
                            }
                            else {
                                //click
                                if(addMode){
                                    if (masterCounter.increment(masterCounter.getCounterAtPosition().getOriginalName(), settingsDataClass.isAutoCalLittleHouse())) {
                                        Toast.makeText(getApplicationContext(), "Completed 1" + getString(R.string.xiaofangzi), Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    masterCounter.decrement(masterCounter.getCounterAtPosition().getOriginalName());
                                }
                                if (hasVibratorFunction) vibrator.vibrate(100);
                                setCounterView();
                            }
                        }
                        break;
                }
                return false;
            }
        });

        if (settingsDataClass.isArrowsNavigation()) {
            buttonRight.setVisibility(View.VISIBLE);
            buttonLeft.setVisibility(View.VISIBLE);
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
        }
        else {
            buttonLeft.setVisibility(View.GONE);
            buttonRight.setVisibility(View.GONE);
        }

        buttonMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingsDataClass.isAddSubButtonMode()) {
                    if(addMode) {
                        addMode = false;
                        buttonMode.setImageResource(R.drawable.ic_add_sign);
                    }
                    else{
                        addMode = true;
                        buttonMode.setImageResource(R.drawable.ic_sub_sign);
                    }
                }
                else {
                    addMode = true;
                    buttonMode.setImageResource(R.drawable.ic_sub_sign);
                    masterCounter.decrement(masterCounter.getCounterAtPosition().getOriginalName());
                    if (hasVibratorFunction) vibrator.vibrate(100);
                    setCounterView();
                }
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeScreen();
            }
        });

        textMantra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("test", "beforeTextChange");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("test", "duringTextChange");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("test", "afterTextChange " + textMantra.getText());
                masterCounter.getCounterAtPosition().setDisplayName(textMantra.getText().toString());
            }
        });

        buttonTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsScreenIntent = new Intent(getApplicationContext(), SettingsScreen.class);
                startActivity(settingsScreenIntent);
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
        setCounterView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("state", "onResume");
        loadDataFromDatabase();
        setCounterView();
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
        textMantra.setText(masterCounter.getCounterAtPosition().getDisplayName());
        buttonCounter.setText(masterCounter.getCounterAtPosition().getCount().toString());
        testviewUP();
    }


    private void testviewUP(){
        t1.setText( masterCounter.getCounters().get(0).getDisplayName() + ": " + (int)masterCounter.getLittleHouse().getCountByName(getString(R.string.dabei)));
        t2.setText(masterCounter.getCounters().get(1).getDisplayName() + ": " + (int)masterCounter.getLittleHouse().getCountByName(getString(R.string.boruo)));
        t3.setText(masterCounter.getCounters().get(2).getDisplayName() + ": " + (int)masterCounter.getLittleHouse().getCountByName(getString(R.string.wangshen)));
        t4.setText( masterCounter.getCounters().get(3).getDisplayName() + ": " + (int)masterCounter.getLittleHouse().getCountByName(getString(R.string.qifo)));
        t5.setText(masterCounter.getLittleHouse().getLittleHouseDisplayName() + ": " + Integer.toString(masterCounter.getLittleHouse().getLittleHouseCount()));
    }

    private void createDataBase(Context context) {
        db = MasterCounterDatabase.getINSTANCE(getApplicationContext());
    }

    /**
     * Tries to load data from database. Return if successful
     * @return true if data was detected and loaded, false otherwise
     */
    private boolean loadDataFromDatabase() {
        if(db.masterCounterDAO().getLittleHouse() == null || db.masterCounterDAO().getSettingsData() == null || db.masterCounterDAO().getAllCounters().size() == 0) {
            return false;
        }
        masterCounter.setCounters(db.masterCounterDAO().getAllCounters());
        masterCounter.setLittleHouse(db.masterCounterDAO().getLittleHouse());
        masterCounter.setPositionCounters(db.masterCounterDAO().getMasterCounterPosition().getPositionCounters());
        settingsDataClass = db.masterCounterDAO().getSettingsData();
        return true;
    }

    private void setDataFromDatabase() {
        db.masterCounterDAO().insertAllCounters(masterCounter.getCounters());
        db.masterCounterDAO().insertLittleHouse(masterCounter.getLittleHouse());
        db.masterCounterDAO().insertCounterPosition(masterCounter);
        db.masterCounterDAO().insertSettingsData(settingsDataClass);
    }

    private void openHomeScreen() {
        Intent homeScreenIntent = new Intent(this, HomeScreenActivity.class);
        startActivity(homeScreenIntent);
    }

    private void inputInitialSettings() {
        settingsDataClass = new SettingsDataClass(false,false, false, false, false, false);
    }
}