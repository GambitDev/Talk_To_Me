package com.gambitdev.talktome.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gambitdev.talktome.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Intent goToRegister = new Intent(SplashActivity.this , RegisterActivity.class);
            startActivity(goToRegister);
            finish();
        } else {
            Intent goToHome = new Intent(SplashActivity.this , LoadContactsActivity.class);
            startActivity(goToHome);
            finish();
        }
    }
}
