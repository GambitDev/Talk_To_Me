package com.gambitdev.talktome.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.gambitdev.talktome.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
            startActivity(intent);
        });
    }
}
