package com.gambitdev.talktome.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gambitdev.talktome.models.User;
import com.gambitdev.talktome.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ContactProfileActivity extends AppCompatActivity {

    User current;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference ref = db.getReference().child("users");
    ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        findViewById(R.id.back_btn).setOnClickListener(v -> finish());

        String contactUid = getIntent().getStringExtra("contact_uid");
        if (contactUid != null) {
            listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    current = dataSnapshot.getValue(User.class);
                    if (current != null) {
                        if (current.getProfilePicUrl() != null) {
                            Picasso.get().load(current.getProfilePicUrl())
                                    .into((ImageView) findViewById(R.id.user_img));
                        }
                        if (current.getStatus() != null) {
                            ((TextView) findViewById(R.id.status)).setText(current.getStatus());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            ref = ref.child(contactUid);
            ref.addValueEventListener(listener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ref.removeEventListener(listener);
    }
}
