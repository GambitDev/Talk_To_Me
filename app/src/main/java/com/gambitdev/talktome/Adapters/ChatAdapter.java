package com.gambitdev.talktome.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.Pojo.Message;
import com.gambitdev.talktome.R;
import com.google.firebase.auth.FirebaseAuth;

public class ChatAdapter extends FirebaseRecyclerAdapter <Message , ChatAdapter.MsgViewHolder> {

    private String userUid;

    private static final int USER_TEXT = 0;
    private static final int CONTACT_TEXT = 1;
    private static final int USER_IMG = 2;
    private static final int CONTACT_IMG = 3;

    public ChatAdapter(FirebaseRecyclerOptions<Message> options) {
        super(options);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
            userUid = mAuth.getCurrentUser().getUid();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getImgMsg() == null) {
            if (getItem(position).getSenderUid().equals(userUid))
                return USER_TEXT;
            else
                return CONTACT_TEXT;
        } else {
            if (getItem(position).getSenderUid().equals(userUid))
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
    protected void onBindViewHolder(@NonNull MsgViewHolder holder, int position, @NonNull Message msg) {
        switch (getItemViewType(position)) {
            case USER_TEXT:
            case CONTACT_TEXT:
                TextMsgViewHolder viewHolder = (TextMsgViewHolder) holder;
                viewHolder.msgTv.setText(msg.getTxtMsg());
                viewHolder.msgTimestamp.setText(msg.getTimestamp());
        }
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
