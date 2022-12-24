package com.nemogz.mantracounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class LittleHouseItemActivity extends AppCompatActivity {

    TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_littlehouse_item_screen);
        test = findViewById(R.id.littleHouseViewTest);
        test.setText("lol");
    }
}