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
    public void symptomLog() {
        Intent intent = new Intent (this, SpeechToText.class);
        startActivity(intent);
    }
}