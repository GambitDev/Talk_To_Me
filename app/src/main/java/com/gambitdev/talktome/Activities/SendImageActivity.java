package com.gambitdev.talktome.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gambitdev.talktome.Models.Message;
import com.gambitdev.talktome.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SendImageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference().child("images");
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference().child("images");

    private int clickCounter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_image);

        progressBar = findViewById(R.id.send_progress);

        Uri imgUri = getIntent().getParcelableExtra("img");
        String contactUid = getIntent().getStringExtra("contact_uid");

        ImageView img = findViewById(R.id.img);
        img.setImageBitmap(getImgBitmap(imgUri));

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        TextInputLayout captionEt = findViewById(R.id.caption_et);
        captionEt.setEndIconOnClickListener(v -> {
            if (clickCounter == 1) return;
            clickCounter++;
            progressBar.setVisibility(View.VISIBLE);
            if (imgUri != null) {
                if (imgUri.getLastPathSegment() != null) {
                    storageRef = storageRef.child(imgUri.getLastPathSegment());
                    UploadTask uploadTask = storageRef.putFile(imgUri);
                    uploadTask.continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SendImageActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        return storageRef.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String downloadUrl;
                            String caption;
                            if (task.getResult() != null) {
                                downloadUrl = task.getResult().toString();
                                if (captionEt.getEditText() != null) {
                                    caption = captionEt.getEditText().getText().toString();
                                    Message newMsg;
                                    if (mAuth.getCurrentUser() != null) {
                                        newMsg = new Message(mAuth.getCurrentUser().getUid(),
                                                caption, downloadUrl);
                                        if (contactUid != null) {
                                            dbRef.child(mAuth.getCurrentUser().getUid())
                                                    .child(contactUid).push().setValue(downloadUrl);
                                            dbRef.child(contactUid)
                                                    .child(mAuth.getCurrentUser().getUid())
                                                    .push().setValue(downloadUrl);
                                        }
                                        String msgJSON = new Gson().toJson(newMsg);
                                        Intent resultData = new Intent().
                                                putExtra("msg_json", msgJSON);
                                        setResult(RESULT_OK, resultData);
                                        finish();
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(SendImageActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private Bitmap getImgBitmap(Uri imageUri) {
        try {
            InputStream imageStream;
            if (imageUri != null) {
                imageStream = getContentResolver().openInputStream(imageUri);
                return BitmapFactory.decodeStream(imageStream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
