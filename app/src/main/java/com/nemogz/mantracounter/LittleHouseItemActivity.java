package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private float TIME_FOR_LONG_CLICK = 500;
    private float RESET_CIRCLE_DIFFERENCE = 175;
    private Boolean addMode = true;
    private MasterCounter masterCounter;
    private SettingsDataClass settingsDataClass;
    private Vibrator vibrator;
    private boolean hasVibratorFunction;
    private SoundPool soundPool;
    private boolean loaded = false;
    private int dingID;
    private int littleHouseID;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("state", "onCreate");
        masterCounter = new MasterCounter(getApplicationContext());
        setContentView(R.layout.activity_littlehouse_screen);
        createDataBase(getApplicationContext());
        inputInitialSettings();
        if (!loadDataFromDatabase()) {
            createEssentailCounters();
        }
        instantiateViews();
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        hasVibratorFunction = vibrator.hasVibrator();
        createMediaPlayer();
        setCounterView();


        //for all types of clicks on Main Button
        littleHouseCountScreen.setOnTouchListener(new View.OnTouchListener() {
            float xStart;
            float yStart;
            float xTrack;
            float yTrack;
            int stage = 0;
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
                    case MotionEvent.ACTION_MOVE:
                        switch (stage) {
                            // down Y increase
                            // right X increase
                            case 0: //start stage
                                if (event.getX() - RESET_CIRCLE_DIFFERENCE > xStart && event.getY() + RESET_CIRCLE_DIFFERENCE < yStart) {
                                    Log.d("motion","Stage 1 - INDEX_MOVE: " + event.getX());
                                    Log.d("motion","Stage 1 - INDEX_MOVE: " + event.getY());
                                    xTrack = event.getX();
                                    yTrack = event.getY();
                                    stage++;
                                }
                                break;
                            case 1: //must move X increase and Y decrease if yes move next stage
                                if (event.getX() + RESET_CIRCLE_DIFFERENCE < xTrack && event.getY() + RESET_CIRCLE_DIFFERENCE < yTrack) {
                                    Log.d("motion","Stage 2 - INDEX_MOVE: " + event.getX());
                                    Log.d("motion","Stage 2 - INDEX_MOVE: " + event.getY());
                                    xTrack = event.getX();
                                    yTrack = event.getY();
                                    stage++;
                                }
                                break;
                            case 2: // must move X decrease Y decrease
                                if (event.getX() + RESET_CIRCLE_DIFFERENCE < xTrack && event.getY() - RESET_CIRCLE_DIFFERENCE > yTrack) {
                                    Log.d("motion","Stage 3 - INDEX_MOVE: " + event.getX());
                                    Log.d("motion","Stage 3 - INDEX_MOVE: " + event.getY());
                                    xTrack = event.getX();
                                    yTrack = event.getY();
                                    stage++;
                                }
                                break;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        xEnd = event.getX();
                        yEnd = event.getY();
                        releasedTime = event.getEventTime();

                        //check for X increase Y icnrease
                        if (stage == 3 && Math.abs(event.getX() - xStart) < RESET_CIRCLE_DIFFERENCE / 2 && Math.abs(event.getY() - yStart) < RESET_CIRCLE_DIFFERENCE / 2) {
                            Log.d("motion","Stage 4 - INDEX_MOVE: " + event.getX());
                            Log.d("motion","Stage 4 - INDEX_MOVE: " + event.getY());
                            xTrack = event.getX();
                            yTrack = event.getY();
                            stage = 0;
                            masterCounter.getLittleHouse().setLittleCount(0);
                            if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(200);
                            setCounterView();
                            break;
                        }

                        //reset whenever motion is up
                        stage = 0;

//                        if (releasedTime - clickedDownTime > TIME_FOR_LONG_CLICK) {
//                            ///long click
//                            masterCounter.getLittleHouse().setLittleCount(0);
//                            if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(200);
//                            setCounterView();
//                        }
//                        else {
                            //click
                            if(addMode){
                                masterCounter.getLittleHouse().setLittleCount(masterCounter.getLittleHouse().getLittleHouseCount() + 1);
                                if (settingsDataClass.isSoundEffect() && loaded) soundPool.play(littleHouseID, 1, 1, 1, 0, 0);
                            }else{
                                masterCounter.getLittleHouse().decrementLittleHouseCount();
                                if (settingsDataClass.isSoundEffect() && loaded) soundPool.play(dingID, 1, 1, 1, 0, 0);
                            }
                            if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(100);
                            setCounterView();
//                        }
                        break;
                }
                return false;
            }
        });

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
                    masterCounter.getLittleHouse().decrementLittleHouseCount();
                    if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(100);
                    if (settingsDataClass.isSoundEffect() && loaded) soundPool.play(dingID, 1, 1, 1, 0, 0);

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

        littleHouseNameScreen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                masterCounter.getLittleHouse().setLittleHouseDisplayName(littleHouseNameScreen.getText().toString());
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
    }

    private void setCounterView(){
        littleHouseNameScreen.setText(masterCounter.getLittleHouse().getLittleHouseDisplayName());
        littleHouseCountScreen.setText(masterCounter.getLittleHouse().getLittleHouseCount().toString());
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
        masterCounter = db.masterCounterDAO().getMasterCounter();
        masterCounter.setCounters(db.masterCounterDAO().getAllCounters());
        masterCounter.setLittleHouse(db.masterCounterDAO().getLittleHouse());
        settingsDataClass = db.masterCounterDAO().getSettingsData();
        return true;
    }

    private void setDataFromDatabase() {
        db.masterCounterDAO().insertAllCounters(masterCounter.getCounters());
        db.masterCounterDAO().insertLittleHouse(masterCounter.getLittleHouse());
        db.masterCounterDAO().insertMasterCounter(masterCounter);
    }

    private void openHomeScreen() {
        Intent homeScreenIntent = new Intent(this, HomeScreenActivity.class);
        startActivity(homeScreenIntent);
    }

    private void inputInitialSettings() {
        if (db.masterCounterDAO().getSettingsData() == null) {
            db.masterCounterDAO().insertSettingsData(new SettingsDataClass(false,false, false, false, false, false, false, false));
        }
    }

    private void createMediaPlayer() {
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Load the sound
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        dingID = soundPool.load(this, R.raw.ding1, 1);
        littleHouseID = soundPool.load(this, R.raw.littlehouse, 1);
    }
}