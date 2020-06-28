package com.gambitdev.talktome.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gambitdev.talktome.Fragments.SlideShowFragment;
import com.gambitdev.talktome.Models.Image;

import java.util.ArrayList;

public class SlideShowAdapter extends FragmentStateAdapter {

    private ArrayList<Image> images = new ArrayList<>();

    public void setImages(ArrayList<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public SlideShowAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new SlideShowFragment(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
