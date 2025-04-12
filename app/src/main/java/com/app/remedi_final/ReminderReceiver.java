package com.app.remedi_final;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReminderReceiver extends BroadcastReceiver {
    private static final String TAG = "ReminderReceiver";
    private static final String PREF_NAME = "medication_prefs";
    private static final String EMERGENCY_CONTACT_KEY = "emergency_contact";

    @Override
    public void onReceive(Context context, Intent intent) {
        int reminderId = intent.getIntExtra("reminder_id", -1);

        if (reminderId != -1) {
            Log.d(TAG, "Received reminder for medication ID: " + reminderId);

            // Check if medication was taken
            if (!isMedicationTaken(context, reminderId)) {
                // Schedule SMS to be sent after 2 minutes if not taken
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (!isMedicationTaken(context, reminderId)) {
                        sendReminderSms(context, reminderId);
                    }
                }, 2 * 60 * 1000); // 2 minutes
            }
        }
    }

    private boolean isMedicationTaken(Context context, int reminderId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = "med_" + reminderId;
        return prefs.getBoolean(key + "_taken", false);
    }

    private void sendReminderSms(Context context, int reminderId) {
        // Get medication details
        SharedPreferences medPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = "med_" + reminderId;
        String medName = medPrefs.getString(key + "_name", "");
        String dose = medPrefs.getString(key + "_dose", "");
        long timeMillis = medPrefs.getLong(key + "_time", 0);

        // Get user information
        SharedPreferences userPrefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String name = userPrefs.getString("name", "User");
        String emergencyContact = userPrefs.getString(EMERGENCY_CONTACT_KEY, "");

        if (medName.isEmpty() || emergencyContact.isEmpty()) {
            Log.e(TAG, "Missing medication data or emergency contact");
            return;
        }

        // Format time
        String timeStr = "";
        if (timeMillis > 0) {
            timeStr = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(timeMillis));
        }

        // Create message
        String message = "MEDICATION REMINDER: " + name +
                " has not taken their medication: " + medName +
                " (" + dose + ") which was scheduled for " + timeStr;

        try {
            // Send SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(emergencyContact, null, message, null, null);
            Log.d(TAG, "SMS sent to emergency contact: " + emergencyContact);
        } catch (Exception e) {
            Log.e(TAG, "Failed to send SMS", e);
        }
    }
}