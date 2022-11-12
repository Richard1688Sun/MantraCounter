package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonMain;
    private int totalMainCount = 0;
//    private Vibrator vibrator;
//    private boolean vibrate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}