package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //to access the strings in classes
    private static Resources resources;

    private Button buttonMain;
    private FloatingActionButton buttonHome;
    private FloatingActionButton buttonMode;
    private FloatingActionButton buttonTool;
    private FloatingActionButton buttonLeft;
    private FloatingActionButton buttonRight;
    private TextView textMantra;
    private int counterIndex = 0;

    private Boolean addMode = true;
//    private Vibrator vibrator;
//    private boolean vibrate = false;
    private List<Counter> counters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
        setContentView(R.layout.counter_screen);

        createEssentailCounters();
        instantiateViews();
        setCounterView();

//        if(vibrator.hasVibrator()){
//            vibrate = true;
//        }

        //detect short press
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addMode){
                    counters.get(counterIndex).increment();
                }else{
                    counters.get(counterIndex).decrement();
                }
                setCounterView();
            }
        });

        //detect long press
        buttonMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                counters.get(counterIndex).setCount(0);
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
                    buttonMode.setImageResource(R.drawable.ic_sub_sign);
                }
                else{
                    addMode = true;
                    buttonMode.setImageResource(R.drawable.ic_add_sign);
                }
            }
        });
    }

    public static Resources getAppResources() {
        return resources;
    }

    public void createEssentailCounters(){
        counters.add(new Counter(getString(R.string.dabei), 0));
        counters.add(new Counter(getString(R.string.boruo), 0));
        counters.add(new Counter(getString(R.string.xiaozai), 0));
        counters.add(new Counter(getString(R.string.qifo), 0));
        Counter.createLittleHouse();
    }

    private void instantiateViews(){
        buttonMain = findViewById(R.id.MainButton);
        buttonHome = findViewById(R.id.HomePageButton);
        buttonLeft = findViewById(R.id.LeftButton);
        buttonRight = findViewById(R.id.RightButton);
        buttonMode = findViewById(R.id.ModeButton);
        buttonTool = findViewById(R.id.ToolBarButton);
        textMantra = findViewById(R.id.MantraNameText);
    }

    private void setCounterView(){
        textMantra.setText(counters.get(counterIndex).getName());
        buttonMain.setText(counters.get(counterIndex).getCount().toString());
    }

    private void incrementCounterIndex(){
        if(counterIndex == counters.size()-1){
            counterIndex = 0;
        }
        else{
            counterIndex++;
        }
    }

    private void decrementCounterIndex(){
        if(counterIndex == 0){
            counterIndex = counters.size() - 1;
        }
        else{
            counterIndex--;
        }
    }
}