package com.gambitdev.talktome.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.Adapters.GalleryAdapter;
import com.gambitdev.talktome.Models.Image;
import com.gambitdev.talktome.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GalleryActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseRecyclerOptions<Image> options;
    private GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        MaterialToolbar toolbar = findViewById(R.id.top_app_bar);
        toolbar.setNavigationOnClickListener(v -> finish());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String userUid = mAuth.getCurrentUser().getUid();
            String contactUid = getIntent().getStringExtra("contact_uid");
            if (contactUid != null) {
                DatabaseReference reference = db.getReference()
                        .child("images")
                        .child(userUid)
                        .child(contactUid);
                options = new FirebaseRecyclerOptions.Builder<Image>()
                        .setQuery(reference, snapshot ->
                                new Image(snapshot.getValue(String.class)))
                        .build();
            }
        }

        RecyclerView gallery = findViewById(R.id.gallery);
        adapter = new GalleryAdapter(options);
        gallery.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
