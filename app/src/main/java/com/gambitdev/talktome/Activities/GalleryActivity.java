package com.gambitdev.talktome.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.Adapters.GalleryAdapter;
import com.gambitdev.talktome.Interfaces.OnGalleryClicked;
import com.gambitdev.talktome.Models.Image;
import com.gambitdev.talktome.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GalleryActivity extends AppCompatActivity implements OnGalleryClicked {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseRecyclerOptions<Image> options;
    private GalleryAdapter adapter;
    private String contactUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        MaterialToolbar toolbar = findViewById(R.id.top_app_bar);
        toolbar.setNavigationOnClickListener(v -> finish());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String userUid = mAuth.getCurrentUser().getUid();
            contactUid = getIntent().getStringExtra("contact_uid");
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
        adapter.setListener(this);
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

    @Override
    public void viewImage(String imgUrl, int pos) {
        Intent intent = new Intent(GalleryActivity.this, ImageSlideShowActivity.class);
        intent.putExtra("img_pos", pos);
        intent.putExtra("contact_uid", contactUid);
        intent.putExtra("img_url", imgUrl);
        startActivity(intent);
    }
}
