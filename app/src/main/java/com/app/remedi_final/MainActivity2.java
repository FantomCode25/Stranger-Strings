package com.app.remedi_final;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {

    private EditText medNameEditText, doseEditText, timeEditText;
    private Button submitButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        medNameEditText = findViewById(R.id.medNameEditText);
        doseEditText = findViewById(R.id.doseEditText);
        timeEditText = findViewById(R.id.timeEditText);
        submitButton = findViewById(R.id.submitButton);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medName = medNameEditText.getText().toString().trim();
                String dose = doseEditText.getText().toString().trim();
                String time = timeEditText.getText().toString().trim();

                if (!medName.isEmpty() && !dose.isEmpty() && !time.isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("medName", medName);
                    resultIntent.putExtra("dose", dose);
                    resultIntent.putExtra("time", time);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}