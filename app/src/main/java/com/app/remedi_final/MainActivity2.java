package com.app.remedi_final;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity2 extends Activity {

    private EditText medicationNameInput;
    private EditText dosageInput;
    private TimePicker timePicker;
    private Button addButton;
    private ListView medicationListView;
    private ArrayList<Medication> medicationList;
    private ArrayAdapter<Medication> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initializeUIComponents();
        medicationList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicationList);
        medicationListView.setAdapter(adapter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMedicationToList();

            }
        });
    }
    private void initializeUIComponents() {
        medicationNameInput = findViewById(R.id.medication_name_input);
        dosageInput = findViewById(R.id.dosage_input);
        timePicker = findViewById(R.id.time_picker);
        addButton = findViewById(R.id.add_button);
        medicationListView = findViewById(R.id.medication_list_view);

        // Set 24-hour view for the time picker (optional)
        timePicker.setIs24HourView(true);
    }
    private void addMedicationToList() {
        // Get user input values
        String medicationName = medicationNameInput.getText().toString().trim();
        String dosageText = dosageInput.getText().toString().trim();

        // Validate input
        if (medicationName.isEmpty() || dosageText.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        double dosage;
        try {
            dosage = Double.parseDouble(dosageText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid dosage", Toast.LENGTH_SHORT).show();
            return;
        }
        int hour, minute;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            // For older Android versions
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        // Create a new Medication object
        Medication newMedication = new Medication(medicationName, dosage, hour, minute);

        // Add to list and update the view
        medicationList.add(newMedication);
        adapter.notifyDataSetChanged();

        // Clear input fields
        clearInputFields();

        // Show confirmation message
        Toast.makeText(this, "Medication added successfully", Toast.LENGTH_SHORT).show();
    }
    private void clearInputFields() {
        medicationNameInput.setText("");
        dosageInput.setText("");
        // Reset focus to medication name field
        medicationNameInput.requestFocus();
    }
    private void clearInputFields() {
        medicationNameInput.setText("");
        dosageInput.setText("");
        // Reset focus to medication name field
        medicationNameInput.requestFocus();
    }

    }
}

