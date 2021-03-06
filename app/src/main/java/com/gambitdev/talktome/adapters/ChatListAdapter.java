package com.gambitdev.talktome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.interfaces.OnChatClicked;
import com.gambitdev.talktome.models.ChatListItem;
import com.gambitdev.talktome.R;
import com.squareup.picasso.Picasso;

public class ChatListAdapter extends FirebaseRecyclerAdapter<ChatListItem , ChatListAdapter.ChatListItemViewHolder> {

    private Context context;
    private OnChatClicked listener;

    public void setListener(OnChatClicked listener) {
        this.listener = listener;
    }

    public ChatListAdapter(@NonNull FirebaseRecyclerOptions<ChatListItem> options , Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatListItemViewHolder holder, int position, @NonNull ChatListItem model) {
        holder.contactName.setText(model.getContactName());
        holder.contactName.setOnClickListener(v ->
                listener.onDisplayNameClick(model.getUid() , model.getContactName()));
        holder.contactName.setOnLongClickListener(v -> {
            listener.onLongClicked(model.getUid(),
                    model.isInPhoneContacts(),
                    model.getPhoneNumber());
            return true;
        });
        if (!model.getLastMsg().isEmpty()) {
            holder.lastMsg.setText(model.getLastMsg());
        } else {
            holder.lastMsg.setText(context.getResources().getString(R.string.img_msg_display));
        }
        holder.lastMsg.setOnClickListener(v ->
                listener.onDisplayNameClick(model.getUid() , model.getContactName()));
        holder.lastMsg.setOnLongClickListener(v -> {
            listener.onLongClicked(model.getUid(),
                    model.isInPhoneContacts(),
                    model.getPhoneNumber());
            return true;
        });
        if (model.getProfilePicUrl() != null)
            Picasso.get().load(model.getProfilePicUrl()).into(holder.profilePic);
        holder.profilePic.setOnClickListener(v ->
                listener.onProfilePicClick(model.getUid()));
        holder.profilePic.setOnLongClickListener(v -> {
            listener.onLongClicked(model.getUid(),
                    model.isInPhoneContacts(),
                    model.getPhoneNumber());
            return true;
        });
    }

    @NonNull
    @Override
    public ChatListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.chat_list_item_layout, parent, false);
            return new ChatListItemViewHolder(view);
    }

    static class ChatListItemViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView contactName , lastMsg;

        ChatListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profile_pic);
            contactName = itemView.findViewById(R.id.contact_name);
            lastMsg = itemView.findViewById(R.id.contact_last_msg);
        }
    }
}
