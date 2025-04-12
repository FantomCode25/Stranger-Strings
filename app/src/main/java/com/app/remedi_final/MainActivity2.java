package com.app.remedi_final;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {

    private EditText medNameEditText, doseEditText, timeEditText;
    private Button submitButton;
    private TextView storedMedicinesTextView;
    private HashMap<String, MedicineDetails> medicineHashMap;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        medNameEditText = findViewById(R.id.medNameEditText);
        doseEditText = findViewById(R.id.doseEditText);
        timeEditText = findViewById(R.id.timeEditText);
        submitButton = findViewById(R.id.submitButton);
        storedMedicinesTextView = findViewById(R.id.storedMedicinesTextView);

        medicineHashMap = new HashMap<>(); // Initialize the HashMap

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medName = medNameEditText.getText().toString().trim();
                String dose = doseEditText.getText().toString().trim();
                String time = timeEditText.getText().toString().trim();

                if (!medName.isEmpty() && !dose.isEmpty() && !time.isEmpty()) {
                    medicineHashMap.put(medName, new MedicineDetails(dose, time));
                    updateDisplay();
                    clearFields();
                }
            }
        });
    }

    private void updateDisplay() {
        StringBuilder displayText = new StringBuilder();
        for (String key : medicineHashMap.keySet()) {
            MedicineDetails details = medicineHashMap.get(key);
            displayText.append(key).append(": Dose - ")
                    .append(details.getDose())
                    .append(", Time - ")
                    .append(details.getTime())
                    .append("\n");
        }
        storedMedicinesTextView.setText(displayText.toString());
    }

    private void clearFields() {
        medNameEditText.setText("");
        doseEditText.setText("");
        timeEditText.setText("");
    }

    static class MedicineDetails {
        private String dose;
        private String time;

        public MedicineDetails(String dose, String time) {
            this.dose = dose;
            this.time = time;
        }

        public String getDose() {
            return dose;
        }

        public String getTime() {
            return time;
        }
    }
}