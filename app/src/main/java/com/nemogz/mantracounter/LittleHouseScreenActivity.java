package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.homeworkScreen.LittleHouseReminderPrompt;
import com.nemogz.mantracounter.settings.SettingsDataClass;

import java.util.ArrayList;
import java.util.List;

public class LittleHouseScreenActivity extends AppCompatActivity {

    public MasterCounterDatabase db;
    private EditText littleHouseNameScreen;
    private Button littleHouseCountScreen;
    private FloatingActionButton buttonHome;
    private FloatingActionButton buttonMode;
    private FloatingActionButton buttonTool;
    private float TIME_FOR_LONG_CLICK = 500;
    private float PIXEL_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    private float RESET_CIRCLE_RADIUS = PIXEL_WIDTH / (float) 6;
    private Boolean addMode = true;
    private MasterCounter masterCounter;
    private SettingsDataClass settingsDataClass;
    private Vibrator vibrator;
    private boolean hasVibratorFunction;
    private SoundPool soundPool;
    private boolean loaded = false;
    private int dingID;
    private int littleHouseID;

    // BELOW IS ALL FOR THE REMINDER PROMPT
    public MasterCounter getMasterCounter() {
        return masterCounter;
    }

    public SettingsDataClass getSettingsDataClass() {
        return settingsDataClass;
    }

    public Vibrator getVibrator() {
        return vibrator;
    }

    public boolean isHasVibratorFunction() {
        return hasVibratorFunction;
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public int getLittleHouseID() {
        return littleHouseID;
    }

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
            List<Float[]> points = new ArrayList<Float[]>();

            float maxX = -1;
            float minX = -1;
            float maxY = -1;
            float minY = -1;
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("motion","Action Down: " + event.getX());
                        Log.d("motion","Action Down: " + event.getY());
                        xStart = event.getX();
                        yStart = event.getY();
                        clickedDownTime = event.getEventTime();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //getting the min and max values of the motion
                        if (maxX < event.getX() || maxX == -1) {
                            maxX = event.getX();
                        }
                        if (minX > event.getX() || minX == -1) {
                            minX = event.getX();
                        }

                        if (maxY < event.getY() || maxY == -1) {
                            maxY = event.getY();
                        }
                        if (minY > event.getY() || minY == -1) {
                            minY = event.getY();
                        }

                        points.add(new Float[]{event.getX(), event.getY()});
                        break;
                    case MotionEvent.ACTION_UP:
                        xEnd = event.getX();
                        yEnd = event.getY();
                        releasedTime = event.getEventTime();

                        float xMaxDiff = maxX - minX;
                        float yMaxDiff = maxY - minY;
                        float xOrigin = (maxX + minX) / 2;
                        float yOrigin = (maxY + minY) / 2;
                        Log.d("motion", "xMaxDiff: " + xMaxDiff);
                        Log.d("motion", "yMaxDiff: " + yMaxDiff);
                        Log.d("motion", "xOrigin: " + xOrigin);
                        Log.d("motion", "yOrigin: " + yOrigin);
                        Log.d("motion", "maxX: " + maxX);
                        Log.d("motion", "minX: " + minX);
                        Log.d("motion", "maxY: " + maxY);
                        Log.d("motion", "minY: " + minY);

                        if (yMaxDiff >= RESET_CIRCLE_RADIUS && xMaxDiff >= RESET_CIRCLE_RADIUS) { //checking the two sides
                            //checking the diagonals
                            float[] topRight = new float[]{xOrigin + RESET_CIRCLE_RADIUS / (float)Math.sqrt(2), yOrigin - RESET_CIRCLE_RADIUS / (float)Math.sqrt(2)};
                            float[] topLeft = new float[]{xOrigin - RESET_CIRCLE_RADIUS / (float)Math.sqrt(2), yOrigin - RESET_CIRCLE_RADIUS / (float)Math.sqrt(2)};
                            float[] bottomRight = new float[]{xOrigin + RESET_CIRCLE_RADIUS / (float)Math.sqrt(2), yOrigin + RESET_CIRCLE_RADIUS / (float)Math.sqrt(2)};
                            float[] bottomLeft = new float[]{xOrigin - RESET_CIRCLE_RADIUS / (float)Math.sqrt(2), yOrigin + RESET_CIRCLE_RADIUS / (float)Math.sqrt(2)};
                            boolean topRightBool = false;
                            boolean topLeftBool = false;
                            boolean bottomRightBool = false;
                            boolean bottomLeftBool = false;

                            //checking if diagonals are satisfied
                            for (Float[] point : points) {
                                if (point[0] > topRight[0] && point[1] < topRight[1]) {
                                    topRightBool = true;
                                    Log.d("motion", "topRightBool is True");
                                }
                                if (point[0] < topLeft[0] && point[1] < topLeft[1]) {
                                    topLeftBool = true;
                                    Log.d("motion", "topLeftBool is True");
                                }
                                if (point[0] > bottomRight[0] && point[1] > bottomRight[1]) {
                                    bottomRightBool = true;
                                    Log.d("motion", "bottomRightBool is True");
                                }
                                if (point[0] < bottomLeft[0] && point[1] > bottomLeft[1]) {
                                    bottomLeftBool = true;
                                    Log.d("motion", "bottomLeftBool is True");
                                }
                            }

                            //checking the end is near the start
                            if (topRightBool && topLeftBool && bottomRightBool && bottomLeftBool) {
                                //reset
                                masterCounter.getLittleHouse().setLittleCount(0);
                                if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(200);
                                setCounterView();

                                //reset the values for the next time
                                minX = -1;
                                maxX = -1;
                                minY = -1;
                                maxY = -1;
                                points.clear();
                                break;
                            }
                        }

                        //reset the values for the next time
                        minX = -1;
                        maxX = -1;
                        minY = -1;
                        maxY = -1;
                        points.clear();

//                        if (releasedTime - clickedDownTime > TIME_FOR_LONG_CLICK) {
//                            ///long click
//                            masterCounter.getLittleHouse().setLittleCount(0);
//                            if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(200);
//                            setCounterView();
//                        }
//                        else {
                            //click
                            if(addMode){
                                if (masterCounter.canIncrementLittleHouse()) {
                                    // littleHouse Incrementation Success
                                    Integer isChecked = new Integer(0);
                                    // TODO: fix the fragment stuff
//                                    ReminderPrompt reminderPrompt = new ReminderPrompt(getString(R.string.confirmAddLittleHouse), isChecked);
                                    LittleHouseReminderPrompt littleHouseReminderPrompt = new LittleHouseReminderPrompt(LittleHouseScreenActivity.this);
                                    littleHouseReminderPrompt.show(getSupportFragmentManager(), "test");

                                }
                                else {
                                    // incrementation failed
                                    if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(200);
                                    Toast.makeText(getApplicationContext(), getString(R.string.failedAddLittleHouse), Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                masterCounter.getLittleHouse().decrementLittleHouseCount();
                                if (settingsDataClass.isSoundEffect() && loaded) soundPool.play(dingID, 1, 1, 1, 0, 0);
                                if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(100);
                            }
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
        setDataToDatabase();
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

    public void setCounterView(){
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

    private void setDataToDatabase() {
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