package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //to access the strings in classes
    private static Resources resources;
    private Button buttonMain;
    private int totalMainCount = 0;

    private Boolean addMode = true;
//    private Vibrator vibrator;
//    private boolean vibrate = false;
    private List<Counter> counters = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
        setContentView(R.layout.activity_main);
        createEssentailCounters();

//        if(vibrator.hasVibrator()){
//            vibrate = true;
//        }

        buttonMain = findViewById(R.id.buttonMain);
        //detect short press
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalMainCount++;
                buttonMain.setText(Integer.toString(totalMainCount));
//                if(vibrate){
//                    vibrator.vibrate(1000);
//                }
            }
        });

        //detect long press
        buttonMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                totalMainCount = 0;
                buttonMain.setText(Integer.toString(totalMainCount));
//                long[] vibrationPatternLongClick = {500,500};
//                if(vibrate){
//                    vibrator.vibrate(vibrationPatternLongClick, 1);
//                }
                return true;
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
    }
}