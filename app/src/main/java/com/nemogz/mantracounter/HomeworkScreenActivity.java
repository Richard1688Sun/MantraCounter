package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.homeworkScreen.HomeworkItemAdapter;
import com.nemogz.mantracounter.settings.SettingsDataClass;

import java.util.List;

public class HomeworkScreenActivity extends AppCompatActivity {

    public MasterCounterDatabase db;
    private EditText homeworkNameScreen;
    private Button homeworkCountScreen;
    private RecyclerView homeworkRecView;
    private FloatingActionButton buttonHome;
    private FloatingActionButton buttonMode;
    private FloatingActionButton buttonTool;
    private float TIME_FOR_LONG_CLICK = 500;

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
        setContentView(R.layout.activity_homework_screen);
        createDataBase(getApplicationContext());
        inputInitialSettings();
        if (!loadDataFromDatabase()) {
            createEssentailCounters();
        }
        instantiateViews();
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        createMediaPlayer();
        hasVibratorFunction = vibrator.hasVibrator();
        setCounterView();

        HomeworkItemAdapter homeworkItemAdapter = new HomeworkItemAdapter(this);
        homeworkItemAdapter.setMasterCounter(masterCounter);

        homeworkRecView.setAdapter(homeworkItemAdapter);

        homeworkRecView.setLayoutManager(new LinearLayoutManager(this));

        //for all types of clicks on Main Button
        homeworkCountScreen.setOnTouchListener(new View.OnTouchListener() {
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
                            masterCounter.resetHomeworkCount();
                            if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(200);
                            setCounterView();
                        }
                        else {
                            //click
                            if(addMode){
                                if (masterCounter.canCompleteHomework()) {
                                    masterCounter.incrementHomework();
                                    homeworkItemAdapter.notifyDataSetChanged();
                                    if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(100);
                                    if (settingsDataClass.isSoundEffect() && loaded) soundPool.play(littleHouseID, 1, 1, 1, 0, 0);

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.cannotCompleteHW), Toast.LENGTH_SHORT).show();
                                    if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(200);
                                }
                            }else{
                                masterCounter.decrementHomework();
                                if (hasVibratorFunction && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(100);
                                if (settingsDataClass.isSoundEffect() && loaded) soundPool.play(dingID, 1, 1, 1, 0, 0);
                            }
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
                    masterCounter.decrementHomework();
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

        homeworkNameScreen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                masterCounter.setHomeworkDisplayName(homeworkNameScreen.getText().toString());
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
        homeworkCountScreen = findViewById(R.id.HomeworkCountScreen);
        buttonHome = findViewById(R.id.HomePageButton);
        buttonMode = findViewById(R.id.ModeButton);
        buttonTool = findViewById(R.id.ToolBarButton);
        homeworkNameScreen = findViewById(R.id.HomeworkNameScreen);
        homeworkRecView = findViewById(R.id.homeworkRecyclerView);
    }

    private void setCounterView(){
        homeworkNameScreen.setText(masterCounter.getHomeworkDisplayName());
        homeworkCountScreen.setText(masterCounter.getHomeworkCount().toString());
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
        MasterCounter mc = db.masterCounterDAO().getMasterCounter();
        masterCounter.setCounters(c);
        masterCounter.setLittleHouse(lh);
        masterCounter.setPositionCounters(mc.getPositionCounters());
        masterCounter.setHomeworkDisplayName(mc.getHomeworkDisplayName());
        masterCounter.setHomeworkCount(mc.getHomeworkCount());
        settingsDataClass = db.masterCounterDAO().getSettingsData();
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