package com.gambitdev.talktome.Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListItemViewHolder> {

    @NonNull
    @Override
    public ChatListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ChatListItemViewHolder extends RecyclerView.ViewHolder {

        public ChatListItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
