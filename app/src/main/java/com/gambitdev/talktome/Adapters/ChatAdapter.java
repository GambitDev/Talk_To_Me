package com.gambitdev.talktome.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gambitdev.talktome.Pojo.Message;
import com.gambitdev.talktome.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;

public class ChatAdapter extends RecyclerView.Adapter <ChatAdapter.MsgViewHolder> {

    private ArrayList<Message> messages;
    private String userUid;

    private static final int USER_TEXT = 0;
    private static final int CONTACT_TEXT = 1;
    private static final int USER_IMG = 2;
    private static final int CONTACT_IMG = 3;

    public ChatAdapter() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userUid = mAuth.getCurrentUser().getUid();
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getImgMsg() == null) {
            if (messages.get(position).getSenderUid().equals(userUid))
                return USER_TEXT;
            else
                return CONTACT_TEXT;
        } else {
            if (messages.get(position).getSenderUid().equals(userUid))
                return USER_IMG;
            else
                return CONTACT_IMG;
        }
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case USER_TEXT:
                view = inflater.inflate(R.layout.user_text_msg , parent , false);
                return new TextMsgViewHolder(view);
            case CONTACT_TEXT:
                view = inflater.inflate(R.layout.contact_text_msg , parent , false);
                return new TextMsgViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
        Message currentMsg = messages.get(position);
        switch (getItemViewType(position)) {
            case USER_TEXT:
            case CONTACT_TEXT:
                TextMsgViewHolder viewHolder = (TextMsgViewHolder) holder;
                viewHolder.msgTv.setText(currentMsg.getTxtMsg());
                viewHolder.msgTimestamp.setText(currentMsg.getTimestamp());
        }
    }

    @Override
    public int getItemCount() {
        if (messages == null)
            return 0;
        else
            return messages.size();
    }

    public void addMsg(Message newMsg) {
        if (messages != null) {
            messages.add(newMsg);
            notifyDataSetChanged();
        }
    }

    public boolean isMsgListInitialized() {
        return messages != null;
    }

    static class MsgViewHolder extends RecyclerView.ViewHolder {

        MsgViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class TextMsgViewHolder extends MsgViewHolder {

        TextView msgTv;
        TextView msgTimestamp;

        TextMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            msgTv = itemView.findViewById(R.id.msg_tv);
            msgTimestamp = itemView.findViewById(R.id.msg_timestamp);
        }
    }

    static class ImgMsgViewHolder extends MsgViewHolder {

        ImgMsgViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
