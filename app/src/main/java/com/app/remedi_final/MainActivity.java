package com.app.remedi_final;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashMap;
import java.util.HashMap;


public class MainActivity extends Activity {

    private static final int REQUEST_CODE = 1;
    private Button addReminderBtn;
    private LinearLayout medsContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addReminderBtn = findViewById(R.id.addReminderBtn);
        medsContainer = findViewById(R.id.medsContainer);

        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity2.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String medName = data.getStringExtra("medName");
            String dose = data.getStringExtra("dose");
            String time = data.getStringExtra("time");


            CheckBox checkBox = new CheckBox(this);
            checkBox.setText("Medicine: " + medName + " | Dose: " + dose + " | Time: " + time);
            checkBox.setTextColor(Color.BLACK);
            checkBox.setTextSize(16);
            medsContainer.addView(checkBox);
        }
    }
}