package com.app.remedi_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }
    public void signup(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
    public void acctdone(View vie) {
        Intent intent1 = new Intent(this, HomePage.class);
        startActivity(intent1);
    }
}