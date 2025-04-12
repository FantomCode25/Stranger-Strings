/*package com.app.remedi_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
    public void acctdone(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}
*/
package com.app.remedi_final;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends Activity {

    private TextInputLayout nameLayout, usernameLayout, phoneNumberLayout, passwordLayout, emergencyContactLayout;
    private TextInputEditText nameInput, usernameInput, phoneNumberInput, passwordInput, emergencyContactInput;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize TextInputLayouts
        nameLayout = findViewById(R.id.textInputLayout3);
        usernameLayout = findViewById(R.id.textInputLayout5);
        phoneNumberLayout = findViewById(R.id.textInputLayout6);
        passwordLayout = findViewById(R.id.textInputLayout7);
        emergencyContactLayout = findViewById(R.id.textInputLayout8);

        // Set hints
        nameLayout.setHint("Full Name");
        usernameLayout.setHint("Username");
        phoneNumberLayout.setHint("Phone Number");
        passwordLayout.setHint("Password");
        emergencyContactLayout.setHint("Emergency Contact Number");

        // Get TextInputEditText references
        nameInput = (TextInputEditText) nameLayout.getEditText();
        usernameInput = (TextInputEditText) usernameLayout.getEditText();
        phoneNumberInput = (TextInputEditText) phoneNumberLayout.getEditText();
        passwordInput = (TextInputEditText) passwordLayout.getEditText();
        emergencyContactInput = (TextInputEditText) emergencyContactLayout.getEditText();

        // Configure password field
        if (passwordInput != null) {
            passwordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        // Set phone number input type
        if (phoneNumberInput != null) {
            phoneNumberInput.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        }

        if (emergencyContactInput != null) {
            emergencyContactInput.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        }

        // Get continue button
        continueButton = findViewById(R.id.button3);
    }

    public void acctdone(View view) {
        // Get input values
        String name = nameInput != null ? nameInput.getText().toString().trim() : "";
        String username = usernameInput != null ? usernameInput.getText().toString().trim() : "";
        String phoneNumber = phoneNumberInput != null ? phoneNumberInput.getText().toString().trim() : "";
        String password = passwordInput != null ? passwordInput.getText().toString().trim() : "";
        String emergencyContact = emergencyContactInput != null ? emergencyContactInput.getText().toString().trim() : "";

        // Validate input
        if (name.isEmpty() || username.isEmpty() || phoneNumber.isEmpty() ||
                password.isEmpty() || emergencyContact.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simple phone number validation
        if (phoneNumber.length() < 10 || emergencyContact.length() < 10) {
            Toast.makeText(this, "Please enter valid phone numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save user information
        saveUserData(name, username, phoneNumber, password, emergencyContact);

        // Continue to HomePage
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    private void saveUserData(String name, String username, String phoneNumber,
                              String password, String emergencyContact) {
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("name", name);
        editor.putString("username", username);
        editor.putString("phone_number", phoneNumber);
        editor.putString("password", password);
        editor.putString("emergency_contact", emergencyContact);

        editor.apply();
    }
}