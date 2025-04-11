package com.app.remedi_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.HashMap;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE = 1;
    private LinearLayout medicationsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        medicationsLayout = findViewById(R.id.medicationsLayout); // Ensure to add LinearLayout in your XML
    }

    public void addReminder(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            HashMap<String, MedicineDetails> medications = (HashMap<String, MedicineDetails>) data.getSerializableExtra("medications");
            displayMedications(medications);
        }
    }

    private void displayMedications(HashMap<String, MedicineDetails> medications) {
        medicationsLayout.removeAllViews(); // Clear previous checkboxes

        for (String medName : medications.keySet()) {
            MedicineDetails details = medications.get(medName);
            String dosage = details.getDose();
            String time = details.getTime();

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(medName + ": Dose - " + dosage + ", Time - " + time);
            medicationsLayout.addView(checkBox); // Add the checkbox to the layout
        }
    }

    // Ensure MedicineDetails class can be serialized
    static class MedicineDetails implements Serializable {
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