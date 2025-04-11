package com.app.remedi_final;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class HomePage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
    }
    public void reminderPage(View v) {
        Intent intent1 = new Intent (this, MainActivity.class);
        startActivity(intent1);
    }
    public void inventoryPage(View vi) {
        Intent intent2 = new Intent (this, Inventory.class);
        startActivity(intent2);
    }
    public void symptomPage(View vie) {
        Intent intent3 = new Intent (this, SpeechToText.class);
        startActivity(intent3);
    }
}