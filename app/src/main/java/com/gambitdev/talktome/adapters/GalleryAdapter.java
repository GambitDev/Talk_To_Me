package com.gambitdev.talktome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.activities.GalleryActivity;
import com.gambitdev.talktome.interfaces.OnGalleryClicked;
import com.gambitdev.talktome.interfaces.Selectable;
import com.gambitdev.talktome.models.Image;
import com.gambitdev.talktome.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GalleryAdapter
        extends FirebaseRecyclerAdapter <Image, GalleryAdapter.ImageViewHolder>
        implements Selectable {

    private Context context;
    private OnGalleryClicked listener;
    private boolean selectionMode, clearSelection;
    private ArrayList<Integer> selectedItems = new ArrayList<>();

    private DatabaseReference reference;

    public void setListener(OnGalleryClicked listener) {
        this.listener = listener;
    }

    public GalleryAdapter(@NonNull FirebaseRecyclerOptions<Image> options,
                          Context context, String contactUid) {
        super(options);
        this.context = context;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        if (mAuth.getCurrentUser() != null)
            reference = db.getReference()
                    .child("images")
                    .child(mAuth.getCurrentUser().getUid())
                    .child(contactUid);
    }

    @Override
    protected void onBindViewHolder(@NonNull ImageViewHolder holder, int position, @NonNull Image model) {
        Picasso.get().load(model.getImgUrl()).into(holder.galleryItem);
        if (clearSelection) {
            holder.selectedIcon.setVisibility(View.GONE);
            holder.card.setStrokeWidth(0);
        }
        holder.galleryItem.setOnClickListener(v -> {
            if (!selectionMode) {
                listener.viewImage(model.getImgUrl(), holder.getLayoutPosition());
                return;
            }
            if (!selectedItems.contains(holder.getLayoutPosition())) {
                select(holder.getLayoutPosition(), holder);
            } else {
                deselect(holder.getLayoutPosition(), holder);
            }
        });
        holder.galleryItem.setOnLongClickListener(v -> {
            if (!selectionMode) {
                clearSelection = false;
                selectionMode = true;
                select(holder.getLayoutPosition(), holder);
            }
            return true;
        });
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.gallery_item_layout, parent, false);
        return new ImageViewHolder(view);
    }

    public void deleteSelectedImages() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> keysForDeletion = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    for (Integer selectedItemPos : selectedItems) {
                        String imgForDeletion = getSnapshots().get(selectedItemPos).getImgUrl();
                        String dataImgUrl = child.getValue(String.class);
                        if (imgForDeletion.equals(dataImgUrl)) {
                            keysForDeletion.add(child.getKey());
                        }
                    }
                }
                for (String key : keysForDeletion) {
                    DatabaseReference imgRef = reference.child(key);
                    imgRef.removeValue();
                }
                ((GalleryActivity) context).closeSelectionOptions();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public int getSelectedCount() {
        return selectedItems.size();
    }

    public void cancelSelection() {
        selectedItems = new ArrayList<>();
        selectionMode = false;
        clearSelection = true;
        notifyDataSetChanged();
    }

    @Override
    public <T extends RecyclerView.ViewHolder> void select(int position, @NotNull T holder) {
        selectedItems.add(position);
        ((ImageViewHolder)holder).selectedIcon.setVisibility(View.VISIBLE);
        ((ImageViewHolder)holder).card.setStrokeWidth(5);
        ((GalleryActivity) context).updateSelectionOptions(selectedItems.size());
    }

    @Override
    public <T extends RecyclerView.ViewHolder> void deselect(int position, @NotNull T holder) {
        selectedItems.remove(Integer.valueOf(position));
        ((ImageViewHolder)holder).selectedIcon.setVisibility(View.GONE);
        ((ImageViewHolder)holder).card.setStrokeWidth(0);
        ((GalleryActivity) context).updateSelectionOptions(selectedItems.size());
        if (selectedItems.size() == 0)
            ((GalleryActivity) context).closeSelectionOptions();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout selectedIcon;
        ImageView galleryItem;
        MaterialCardView card;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            selectedIcon = itemView.findViewById(R.id.selected_icon);
            galleryItem = itemView.findViewById(R.id.img);
            card = itemView.findViewById(R.id.root_card);
        }
    }
}
