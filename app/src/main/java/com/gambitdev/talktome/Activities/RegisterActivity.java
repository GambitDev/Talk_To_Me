package com.gambitdev.talktome.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gambitdev.talktome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

public class RegisterActivity extends AppCompatActivity{

    private FirebaseAuth auth;

    Button registerBtn;
    EditText phoneEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        phoneEt = findViewById(R.id.phoneEt);
        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(v -> {
            String phoneNumber = phoneEt.getText().toString();
            if (phoneNumber.isEmpty()) {
                Toast.makeText(this,
                        "Please enter a valid phone number",
                        Toast.LENGTH_SHORT).show();
            } else {

            }
        });
    }
}
