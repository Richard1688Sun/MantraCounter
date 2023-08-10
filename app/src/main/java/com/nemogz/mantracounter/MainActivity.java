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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nemogz.mantracounter.counterStuff.Counter;
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
    private SoundPool soundPool;
    private boolean loaded = false;
    private int dingID;
    private int littleHouseID;
    private float DISTANCE_FOR_SWIPE = 550;
    private float TIME_FOR_LONG_CLICK = 2000;
    private float RESET_CIRCLE_DIFFERENCE = 175;

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
        createMediaPlayer();
        setCounterView();

        setSideBar();

        //for all types of clicks on Main Button
        buttonCounter.setOnTouchListener(new View.OnTouchListener() {
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

                        float xDiff = xStart - xEnd;
                        float yDiff = yStart - yEnd;

                        //check for X increase Y icnrease
                        if (stage == 3 && Math.abs(event.getX() - xStart) < RESET_CIRCLE_DIFFERENCE / 2 && Math.abs(event.getY() - yStart) < RESET_CIRCLE_DIFFERENCE / 2) {
                            Log.d("motion","Stage 4 - INDEX_MOVE: " + event.getX());
                            Log.d("motion","Stage 4 - INDEX_MOVE: " + event.getY());
                            xTrack = event.getX();
                            yTrack = event.getY();
                            stage = 0;
                            masterCounter.setCount(masterCounter.getCounterAtPosition().getOriginalName(), 0);
                            masterCounter.getLittleHouse().setLittleHouseByName(masterCounter.getCounterAtPosition().getOriginalName(), 0);
                            if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(200);
                            setCounterView();
                            break;
                        }

                        //reset whenever motion is up
                        stage = 0;

                        //other default operations
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
//                            if (releasedTime - clickedDownTime > TIME_FOR_LONG_CLICK) {
//                                ///long click
//                                masterCounter.setCount(masterCounter.getCounterAtPosition().getOriginalName(), 0);
//                                masterCounter.getLittleHouse().setLittleHouseByName(masterCounter.getCounterAtPosition().getOriginalName(), 0);
//                                if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(200);
//                                setCounterView();
//                            }
//                            if {
                                //click
                                if(addMode){
                                    boolean bool = masterCounter.increment(masterCounter.getCounterAtPosition().getOriginalName(), settingsDataClass.isAutoCalLittleHouse());
                                    if (bool) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.Completed)+ " 1" + " " + getString(R.string.xiaofangzi), Toast.LENGTH_SHORT).show();
                                        if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(1000);
                                        if (settingsDataClass.isSoundEffect() && loaded) soundPool.play(littleHouseID, 1, 1, 1, 0, 0);
                                        setCounterView();
                                        break;
                                    }
                                    else {
                                        if (settingsDataClass.isSoundEffect() && loaded) soundPool.play(dingID, 1, 1, 1, 0, 0);
                                    }
                                }else{
                                    masterCounter.decrement(masterCounter.getCounterAtPosition().getOriginalName());
                                    if (settingsDataClass.isSoundEffect() && loaded) soundPool.play(dingID, 1, 1, 1, 0, 0);
                                }
                                if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(100);
                                setCounterView();
//                            }
                        }
                        break;
                }
                return false;
            }
        });

        setNavArrows();

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

        textMantra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
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
        testViewUP();
        setNavArrows();
        setSideBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("state", "onResume");
        loadDataFromDatabase();
        setCounterView();
        testViewUP();
        setNavArrows();
        setSideBar();
    }

    public static Resources getAppResources() {
        return resources;
    }

    public void createEssentailCounters(){
        masterCounter = new MasterCounter(getApplicationContext());
        masterCounter.createBasicCounters();
    }

    private void setNavArrows() {
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
    }

    private void setSideBar() {
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
        testViewUP();
    }


    /**
     * displays the theoretical count amounts if there was autoLittleHouse conversions
     */
    private void testViewUP(){
        if (settingsDataClass.isAutoCalLittleHouse()) {
            t1.setText(masterCounter.getCounters().get(0).getDisplayName() + ": " + masterCounter.getLittleHouse().getLittleHouseMap().get(getString(R.string.dabei)).intValue() + " -> " + (masterCounter.getCounters().get(0).getCount() - masterCounter.getLittleHouse().getLittleHouseMap().get(getString(R.string.dabei)).intValue()* Counter.DaBeiLimit));
            t2.setText(masterCounter.getCounters().get(1).getDisplayName() + ": " + masterCounter.getLittleHouse().getLittleHouseMap().get(getString(R.string.boruo)).intValue() + " -> " + (masterCounter.getCounters().get(1).getCount() - masterCounter.getLittleHouse().getLittleHouseMap().get(getString(R.string.boruo)).intValue()* Counter.BoRuoLimit));
            t3.setText(masterCounter.getCounters().get(2).getDisplayName() + ": " + masterCounter.getLittleHouse().getLittleHouseMap().get(getString(R.string.wangshen)).intValue() + " -> " + (masterCounter.getCounters().get(2).getCount() - masterCounter.getLittleHouse().getLittleHouseMap().get(getString(R.string.wangshen)).intValue()* Counter.WangShenLimit));
            t4.setText(masterCounter.getCounters().get(3).getDisplayName() + ": " + masterCounter.getLittleHouse().getLittleHouseMap().get(getString(R.string.qifo)).intValue() + " -> " + (masterCounter.getCounters().get(3).getCount() - masterCounter.getLittleHouse().getLittleHouseMap().get(getString(R.string.qifo)).intValue()* Counter.QiFoLimit));
            t5.setText(masterCounter.getLittleHouse().getLittleHouseDisplayName() + ": " + masterCounter.getLittleHouse().getLittleHouseCount());
        }
        else {
            int completed = masterCounter.getLittleHouse().findLittleHouseCompleted();
            t1.setText(masterCounter.getCounters().get(0).getDisplayName() + ": " + masterCounter.getCounters().get(0).getCount() + " -> " +(masterCounter.getCounters().get(0).getCount() -
                    masterCounter.getCounterAtPosition().DaBeiLimit*completed));

            t2.setText(masterCounter.getCounters().get(1).getDisplayName() + ": " + masterCounter.getCounters().get(1).getCount() + " -> "+ (masterCounter.getCounters().get(1).getCount() -
                    masterCounter.getCounterAtPosition().BoRuoLimit*completed));

            t3.setText(masterCounter.getCounters().get(2).getDisplayName() + ": " + masterCounter.getCounters().get(2).getCount() + " -> "+ (masterCounter.getCounters().get(2).getCount() -
                    masterCounter.getCounterAtPosition().WangShenLimit*completed));

            t4.setText( masterCounter.getCounters().get(3).getDisplayName() + ": " + masterCounter.getCounters().get(3).getCount() + " -> "+ (masterCounter.getCounters().get(3).getCount() -
                    masterCounter.getCounterAtPosition().QiFoLimit*completed));

            t5.setText(masterCounter.getLittleHouse().getLittleHouseDisplayName() + ": " + masterCounter.getLittleHouse().getLittleHouseCount() + " -> "+ (completed + masterCounter.getLittleHouse().getLittleHouseCount()));
        }
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
//        masterCounter.setPositionCounters(mc.getPositionCounters());
//        masterCounter.setHomeworkDisplayName(mc.getHomeworkDisplayName());
//        masterCounter.setHomeworkCount(mc.getHomeworkCount());
        return true;
    }

    private void setDataFromDatabase() {
        db.masterCounterDAO().insertAllCounters(masterCounter.getCounters());
        db.masterCounterDAO().insertLittleHouse(masterCounter.getLittleHouse());
        db.masterCounterDAO().insertMasterCounter(masterCounter);
        db.masterCounterDAO().insertSettingsData(settingsDataClass);
    }

    private void openHomeScreen() {
        Intent homeScreenIntent = new Intent(this, HomeScreenActivity.class);
        startActivity(homeScreenIntent);
    }

    private void inputInitialSettings() {
        settingsDataClass = new SettingsDataClass(false,false, false, false, false, false, false, false);
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