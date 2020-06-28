package com.gambitdev.talktome.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gambitdev.talktome.Models.Image;
import com.gambitdev.talktome.R;
import com.squareup.picasso.Picasso;

public class SlideShowFragment extends Fragment {

    private Image image;

    public SlideShowFragment(Image image) {
        this.image = image;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slide_show_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView imageView = view.findViewById(R.id.img);
        Picasso.get().load(image.getImgUrl()).into(imageView);
    }
}
