package com.app.remedi_final;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;

import java.util.Date;

public class MedicationReminder {
    private final int id;
    private final String medName;
    private final String dose;
    private final Date reminderTime;
    private final CheckBox checkBox;
    private boolean taken;
    private final String name;

    public MedicationReminder(int id, String medName, String dose, Date reminderTime, CheckBox checkBox) {
        this.id = id;
        this.medName = medName;
        this.dose = dose;
        this.reminderTime = reminderTime;
        this.checkBox = checkBox;
        this.taken = false;

        Context context = checkBox.getContext();
        SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        this.name = prefs.getString("name", "User");
    }

    public int getId() { return id; }
    public String getMedName() { return medName; }
    public String getDose() { return dose; }
    public Date getReminderTime() { return reminderTime; }
    public CheckBox getCheckBox() { return checkBox; }
    public boolean isTaken() { return taken; }
    public void setTaken(boolean taken) { this.taken = taken; }
    public String getName() { return name; }
}
