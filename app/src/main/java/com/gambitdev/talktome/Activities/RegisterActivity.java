package com.gambitdev.talktome.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gambitdev.talktome.R;

public class RegisterActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText phoneEt = findViewById(R.id.phoneEt);
        Button registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(v -> {
            String phoneNumber = phoneEt.getText().toString();
            if (phoneNumber.length() != 10) {
                phoneEt.setError(getResources().getString(R.string.phone_number_error));
            } else {
                Intent goToVerification = new Intent(RegisterActivity.this , VerificationActivity.class);
                goToVerification.putExtra("phone_number" , phoneNumber);
                startActivity(goToVerification);
                finish();
            }
        });
    }
}
