package com.gambitdev.talktome.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.Models.Image;
import com.gambitdev.talktome.R;
import com.squareup.picasso.Picasso;

public class GalleryAdapter extends FirebaseRecyclerAdapter <Image, GalleryAdapter.ImageViewHolder> {

    public GalleryAdapter(@NonNull FirebaseRecyclerOptions<Image> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ImageViewHolder holder, int position, @NonNull Image model) {
        Picasso.get().load(model.getImgUrl()).into(holder.galleryItem);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.gallery_item_layout, parent, false);
        return new ImageViewHolder(view);
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView galleryItem;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            galleryItem = itemView.findViewById(R.id.img);
        }
    }
}
