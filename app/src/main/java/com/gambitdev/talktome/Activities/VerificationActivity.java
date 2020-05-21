package com.gambitdev.talktome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gambitdev.talktome.Pojo.User;
import com.gambitdev.talktome.R;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    private String mVerificationId;
    private EditText verificationCodeEt;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("users");
        mAuth = FirebaseAuth.getInstance();
        verificationCodeEt = findViewById(R.id.verification_code_et);
        String phoneNumber = getIntent().getStringExtra("phone_number");
        sendVerificationCode(phoneNumber);

        findViewById(R.id.verification_btn).setOnClickListener(v -> {
            String code = verificationCodeEt.getText().toString();
            if (code.length() != 6) {
                verificationCodeEt.setError(getResources().getString(R.string.code_error));
                verificationCodeEt.requestFocus();
            } else {
                verifyVerificationCode(code);
            }
        });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        verificationCodeEt.setText(code);
                        verifyVerificationCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String s,
                                       @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    mVerificationId = s;
                }
            };

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerificationActivity.this, task -> {
                    if (task.isSuccessful()) {
                        addUserToDatabase();
                        Intent intent = new Intent(VerificationActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        String message = "Something went wrong";
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered";
                        }

                        Snackbar snackbar = Snackbar.make(findViewById(R.id.parent_view), message, Snackbar.LENGTH_LONG);
                        snackbar.setAction("Dismiss", v ->
                                snackbar.dismiss());
                        snackbar.show();
                    }
                });
    }

    private void addUserToDatabase() {
        if (mAuth.getCurrentUser() != null) {
            User user = new User(mAuth.getCurrentUser().getUid(),
                    mAuth.getCurrentUser().getPhoneNumber());
            ref.push().setValue(user);
        }
    }
}