package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;

import java.util.List;

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
    private int counterIndex = 0;

    private Boolean addMode = true;
    private MasterCounter masterCounter;
//    private Vibrator vibrator;
//    private boolean vibrate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
        setContentView(R.layout.counter_screen);
        createDataBase(getApplicationContext());
        masterCounter = new MasterCounter(getApplicationContext());
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
                    masterCounter.increment(masterCounter.getCounters().get(counterIndex).getName());
                }else{
                    masterCounter.decrement(masterCounter.getCounters().get(counterIndex).getName());;
                }
                setCounterView();
            }
        });

        //detect long press
        buttonCounter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                masterCounter.setCount(masterCounter.getCounters().get(counterIndex).getName(), 0);
                setCounterView();
                return true;
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementCounterIndex();
                setCounterView();
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementCounterIndex();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.masterCounterDAO().insertAllCounters(masterCounter.getCounters());
        db.masterCounterDAO().insertLittleHouse(masterCounter.getLittleHouse());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadDataFromDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        textMantra.setText(masterCounter.getCounters().get(counterIndex).getName());
        buttonCounter.setText(masterCounter.getCounters().get(counterIndex).getCount().toString());
        testviewUP();
    }

    /**
     * Increments the counter Index(index that is displayed)
     */
    private void incrementCounterIndex(){
        if(counterIndex == masterCounter.getCounters().size()-1){
            counterIndex = 0;
        }
        else{
            counterIndex++;
        }
    }

    /**
     * Decrements the counter Index(index that is displayed)
     */
    private void decrementCounterIndex(){
        if(counterIndex == 0){
            counterIndex = masterCounter.getCounters().size() - 1;
        }
        else{
            counterIndex--;
        }
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