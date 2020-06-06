package com.gambitdev.talktome.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gambitdev.talktome.R;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ImageButton backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> finish());

        String imgUrl = getIntent().getStringExtra("img_url");
        ImageView imageView = findViewById(R.id.img);
        if (imgUrl != null)
            Picasso.get().load(imgUrl).into(imageView);
        else
            imageView.setImageDrawable(getDrawable(R.drawable.profile_pic_default));
    }
}
