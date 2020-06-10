package com.gambitdev.talktome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.gambitdev.talktome.R;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final TextInputLayout phoneEt = findViewById(R.id.phoneEt);
        Button registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(v -> {
            if (phoneEt.getEditText() != null) {
                String phoneNumber = phoneEt.getEditText().getText().toString();
                if (phoneNumber.isEmpty()) {
                    phoneEt.setError(getResources().getString(R.string.phone_number_error));
                } else {
                    Intent goToVerification = new Intent(RegisterActivity.this, VerificationActivity.class);
                    goToVerification.putExtra("phone_number", phoneNumber);
                    startActivity(goToVerification);
                }
            }
        });
    }
}
