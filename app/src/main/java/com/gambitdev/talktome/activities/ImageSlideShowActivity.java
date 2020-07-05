package com.gambitdev.talktome.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.gambitdev.talktome.adapters.SlideShowAdapter;
import com.gambitdev.talktome.models.Image;
import com.gambitdev.talktome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImageSlideShowActivity extends AppCompatActivity {

    private SlideShowAdapter adapter;
    private DatabaseReference reference;
    private ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slide_show);

        findViewById(R.id.back_btn).setOnClickListener(v -> finish());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String contactUid = getIntent().getStringExtra("contact_uid");
            if (contactUid != null) {
                reference = db.getReference()
                        .child("images")
                        .child(mAuth.getCurrentUser().getUid())
                        .child(contactUid);
            }

            eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Image> images = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        images.add(new Image(snapshot.getValue(String.class)));
                    }
                    adapter.setImages(images);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            if (reference != null)
                reference.addListenerForSingleValueEvent(eventListener);
        }

        ViewPager2 slideShow = findViewById(R.id.slide_show);
        adapter = new SlideShowAdapter(this);
        slideShow.setAdapter(adapter);
        int startPos = getIntent().getIntExtra("img_pos", -1);
        if (startPos != - 1) {
            slideShow.post(new Runnable() {
                @Override
                public void run() {
                    slideShow.setCurrentItem(startPos);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(eventListener);
    }
}
