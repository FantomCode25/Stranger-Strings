package com.app.remedi_final;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Checkbox extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 100;
    private CheckBox triggerCheckBox;

    // Sample phone number (replace with dynamic logic as needed)
    private final String phoneNumber = "1234567890";  // Replace this with a real number or dynamic source

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Ensure your layout contains trigger_checkbox1

        triggerCheckBox = findViewById(R.id.trigger_checkbox1);

        // Request SMS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }

        triggerCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (triggerCheckBox.isChecked()) {
                    sendSms(phoneNumber, "Your loved one has taken their medication.");
                }
            }
        });
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
