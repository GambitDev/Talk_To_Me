package com.gambitdev.talktome.Adapters;

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
import com.gambitdev.talktome.Interfaces.OnContactClick;
import com.gambitdev.talktome.Models.ChatListItem;
import com.gambitdev.talktome.R;
import com.squareup.picasso.Picasso;

public class ChatListAdapter extends FirebaseRecyclerAdapter<ChatListItem , ChatListAdapter.ChatListItemViewHolder> {

    private Context context;
    private OnContactClick listener;

    public void setListener(OnContactClick listener) {
        this.listener = listener;
    }

    public ChatListAdapter(@NonNull FirebaseRecyclerOptions<ChatListItem> options , Context context) {
        super(options);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getContactName() == null) return 0;
        else return 1;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatListItemViewHolder holder, int position, @NonNull ChatListItem model) {
        holder.contactName.setText(model.getContactName());
        holder.contactName.setOnClickListener(v ->
                listener.onDisplayNameClick(model.getUid() , model.getContactName()));
        if (!model.getLastMsg().isEmpty()) {
            holder.lastMsg.setText(model.getLastMsg());
        } else {
            holder.lastMsg.setText(context.getResources().getString(R.string.img_msg_display));
        }
        holder.lastMsg.setOnClickListener(v ->
                listener.onDisplayNameClick(model.getUid() , model.getContactName()));
        if (model.getProfilePicUrl() != null)
            Picasso.get().load(model.getProfilePicUrl()).into(holder.profilePic);
        holder.profilePic.setOnClickListener(v ->
                listener.onProfilePicClick());
    }

    @NonNull
    @Override
    public ChatListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.chat_list_item_layout, parent, false);
            return new ChatListItemViewHolder(view);
        } else return null;
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
