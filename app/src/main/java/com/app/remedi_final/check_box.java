package com.app.remedi_final;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class check_box extends Activity {

    private static final String TAG = "check_box";
    private static final String PREF_NAME = "medication_prefs";

    private LinearLayout medsContainer;
    private List<MedicationReminder> reminderList;
    private Handler handler;

    // Keys for shared preferences
    private static final String EMERGENCY_CONTACT_KEY = "emergency_contact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        medsContainer = findViewById(R.id.medsContainer);
        reminderList = new ArrayList<>();
        handler = new Handler();

        Button addReminderBtn = findViewById(R.id.addReminderBtn);
        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(check_box.this, MainActivity2.class);
                startActivityForResult(intent, 1);
            }
        });

        // Initialize midnight reset
        scheduleMidnightReset();

        // Check if we need to handle medication reminders
        Intent intent = getIntent();
        if (intent.hasExtra("reset_checkboxes")) {
            resetAllCheckboxes();
        }

        // Load saved medications if any
        loadSavedMedications();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String medName = data.getStringExtra("medName");
            String dose = data.getStringExtra("dose");
            String time = data.getStringExtra("time");

            if (medName != null && dose != null && time != null) {
                addMedicationReminder(medName, dose, time);
            }
        }
    }

    private void addMedicationReminder(String medName, String dose, String time) {
        // Create a unique ID for this medication
        int reminderId = (medName + dose + time).hashCode();

        // Create container for this medication item
        LinearLayout medicationItem = new LinearLayout(this);
        medicationItem.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        medicationItem.setOrientation(LinearLayout.VERTICAL);
        medicationItem.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        medicationItem.setBackgroundColor(Color.parseColor("#f0f0f0"));

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) medicationItem.getLayoutParams();
        layoutParams.bottomMargin = dpToPx(8);
        medicationItem.setLayoutParams(layoutParams);

        // Create checkbox
        CheckBox medicationCheckBox = new CheckBox(this);
        medicationCheckBox.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        medicationCheckBox.setText(medName);
        medicationCheckBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        medicationCheckBox.setTextColor(Color.BLACK);
        medicationCheckBox.setPadding(dpToPx(8), 0, dpToPx(8), 0);
        medicationCheckBox.setTag(reminderId);

        // Create details text view
        TextView medicationDetails = new TextView(this);
        LinearLayout.LayoutParams detailsParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        medicationDetails.setLayoutParams(detailsParams);
        medicationDetails.setText("Dose: " + dose + " | Time: " + time);
        medicationDetails.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        medicationDetails.setTextColor(Color.parseColor("#444444"));
        medicationDetails.setPadding(dpToPx(36), 0, dpToPx(8), 0);

        // Add views to container
        medicationItem.addView(medicationCheckBox);
        medicationItem.addView(medicationDetails);

        // Add to medsContainer
        medsContainer.addView(medicationItem);

        // Add checkbox listener
        medicationCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Mark medication as taken
                for (MedicationReminder reminder : reminderList) {
                    if (reminder.getId() == reminderId) {
                        reminder.setTaken(true);

                        // Update in preferences
                        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        String key = "med_" + reminderId;
                        editor.putBoolean(key + "_taken", true);
                        editor.apply();

                        showToast("Medication marked as taken: " + reminder.getMedName());
                        break;
                    }
                }
            }
        });

        // Parse time
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date reminderTime = format.parse(time);

            if (reminderTime != null) {
                // Create reminder object
                MedicationReminder reminder = new MedicationReminder(
                        reminderId, medName, dose, reminderTime, medicationCheckBox);

                // Add to list
                reminderList.add(reminder);

                // Schedule reminder
                scheduleReminder(reminder);

                // Save to preferences
                saveMedicationToPrefs(reminder);
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing time: " + time, e);
            showToast("Invalid time format. Please use HH:MM format.");
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void scheduleReminder(final MedicationReminder reminder) {
        // Calculate when to check if medication is taken
        Calendar now = Calendar.getInstance();
        Calendar reminderCal = Calendar.getInstance();
        reminderCal.setTime(reminder.getReminderTime());

        // Set reminder time for today
        reminderCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
        reminderCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
        reminderCal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

        // If time already passed today, schedule for tomorrow
        if (reminderCal.before(now)) {
            reminderCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        final long reminderTimeMillis = reminderCal.getTimeInMillis();
        final long delayMillis = reminderTimeMillis - System.currentTimeMillis();

        // Schedule reminder check
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!reminder.isTaken()) {
                    // Schedule SMS for 2 minutes later if not taken
                    scheduleSmsReminder(reminder);
                }

                // Reschedule for next day
                handler.postDelayed(this, 24 * 60 * 60 * 1000); // 24 hours
            }
        }, delayMillis);

        // Also use AlarmManager as a backup
        setAlarmForReminder(reminder, reminderTimeMillis);
    }

    private void scheduleSmsReminder(final MedicationReminder reminder) {
        // Schedule SMS 2 minutes after reminder time
        handler.postDelayed(() -> {
            if (!reminder.isTaken()) {
                sendReminderSms(reminder);
            }
        }, 2 * 60 * 1000); // 2 minutes
    }

    private void sendReminderSms(MedicationReminder reminder) {
        // Get emergency contact from shared preferences
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String emergencyContact = prefs.getString(EMERGENCY_CONTACT_KEY, "");

        if (emergencyContact.isEmpty()) {
            Log.e(TAG, "No emergency contact found");
            return;
        }

        // Create message
        String message = "MEDICATION REMINDER: " + reminder.getName() +
                " has not taken their medication: " + reminder.getMedName() +
                " (" + reminder.getDose() + ") which was scheduled for " +
                new SimpleDateFormat("HH:mm", Locale.getDefault()).format(reminder.getReminderTime());

        try {
            // Send SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(emergencyContact, null, message, null, null);
            Log.d(TAG, "SMS sent to emergency contact: " + emergencyContact);
        } catch (Exception e) {
            Log.e(TAG, "Failed to send SMS", e);
        }
    }

    private void setAlarmForReminder(MedicationReminder reminder, long triggerAtMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("reminder_id", reminder.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                reminder.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }

    private void scheduleMidnightReset() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set calendar to next midnight
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        Intent intent = new Intent(this, MidnightResetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.d(TAG, "Midnight reset scheduled for: " + calendar.getTime());
        }
    }

    public void resetAllCheckboxes() {
        for (MedicationReminder reminder : reminderList) {
            CheckBox checkBox = reminder.getCheckBox();
            if (checkBox != null) {
                checkBox.setChecked(false);
            }
            reminder.setTaken(false);

            // Update in preferences
            SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            String key = "med_" + reminder.getId();
            editor.putBoolean(key + "_taken", false);
            editor.apply();
        }
    }

    private void saveMedicationToPrefs(MedicationReminder reminder) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save basic information
        String key = "med_" + reminder.getId();
        editor.putString(key + "_name", reminder.getMedName());
        editor.putString(key + "_dose", reminder.getDose());
        editor.putLong(key + "_time", reminder.getReminderTime().getTime());
        editor.putBoolean(key + "_taken", reminder.isTaken());

        editor.apply();
    }

    private void loadSavedMedications() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // Get all saved medication keys (a better approach would be to save a list of all medication IDs)
        // For simplicity, we'll assume medications have consecutive IDs starting from 1

        // In a real application, you'd want to store a list of all medication IDs
        // This is a simplified approach
        for (int i = 1; i <= 100; i++) { // Arbitrary limit
            String key = "med_" + i;
            String name = prefs.getString(key + "_name", null);

            if (name != null) {
                String dose = prefs.getString(key + "_dose", "");
                long timeMillis = prefs.getLong(key + "_time", 0);
                boolean taken = prefs.getBoolean(key + "_taken", false);

                Date time = new Date(timeMillis);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String timeStr = format.format(time);

                addMedicationReminder(name, dose, timeStr);

                // If already taken, update the checkbox
                if (taken) {
                    for (MedicationReminder reminder : reminderList) {
                        if (reminder.getId() == i) {
                            CheckBox checkBox = reminder.getCheckBox();
                            if (checkBox != null) {
                                checkBox.setChecked(true);
                            }
                            reminder.setTaken(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Inner class to hold medication reminder information
    public static class MedicationReminder {
        private final int id;
        private final String medName;
        private final String dose;
        private final Date reminderTime;
        private final CheckBox checkBox;
        private boolean taken;
        private String name; // User's name

        public MedicationReminder(int id, String medName, String dose, Date reminderTime, CheckBox checkBox) {
            this.id = id;
            this.medName = medName;
            this.dose = dose;
            this.reminderTime = reminderTime;
            this.checkBox = checkBox;
            this.taken = false;

            // Get user's name from preferences
            Context context = checkBox.getContext();
            SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
            this.name = prefs.getString("name", "User");
        }

        public int getId() {
            return id;
        }

        public String getMedName() {
            return medName;
        }

        public String getDose() {
            return dose;
        }

        public Date getReminderTime() {
            return reminderTime;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public boolean isTaken() {
            return taken;
        }

        public void setTaken(boolean taken) {
            this.taken = taken;
        }

        public String getName() {
            return name;
        }
    }
}