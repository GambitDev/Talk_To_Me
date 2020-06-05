package com.gambitdev.talktome.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.Activities.ChatActivity;
import com.gambitdev.talktome.Interfaces.OnMessageClick;
import com.gambitdev.talktome.Models.Message;
import com.gambitdev.talktome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ChatAdapter extends FirebaseRecyclerAdapter <Message , ChatAdapter.MsgViewHolder> {

    private String userUid;
    private OnMessageClick listener;

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

    public void setListener(OnMessageClick listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getImgUrl() == null) {
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
            case USER_IMG:
                view = inflater.inflate(R.layout.user_img_msg , parent , false);
                return new ImgMsgViewHolder(view);
            case CONTACT_IMG:
                view = inflater.inflate(R.layout.contact_img_msg , parent , false);
                return new ImgMsgViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull MsgViewHolder holder, int position, @NonNull Message msg) {
        switch (getItemViewType(position)) {
            case USER_TEXT:
            case CONTACT_TEXT:
                TextMsgViewHolder textViewHolder = (TextMsgViewHolder) holder;
                textViewHolder.msgTv.setText(msg.getTxtMsg());
                break;
            case USER_IMG:
            case CONTACT_IMG:
                ImgMsgViewHolder imgViewHolder = (ImgMsgViewHolder) holder;
                Picasso.get().load(msg.getImgUrl()).into(imgViewHolder.img, new Callback() {
                    @Override
                    public void onSuccess() {
                        ((ChatActivity)holder.itemView.getContext()).scrollDown();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                imgViewHolder.img.setOnClickListener(v ->
                        listener.onImageMessageClicked(msg.getImgUrl()));
                if (msg.getTxtMsg().isEmpty()) {
                    imgViewHolder.caption.setVisibility(View.GONE);
                } else {
                    imgViewHolder.caption.setVisibility(View.VISIBLE);
                    imgViewHolder.caption.setText(msg.getTxtMsg());
                }
                break;
        }
    }

    static class MsgViewHolder extends RecyclerView.ViewHolder {

        MsgViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class TextMsgViewHolder extends MsgViewHolder {

        TextView msgTv;

        TextMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            msgTv = itemView.findViewById(R.id.msg_tv);
        }
    }

    static class ImgMsgViewHolder extends MsgViewHolder {

        ImageView img;
        TextView caption;

        ImgMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            caption = itemView.findViewById(R.id.img_caption);
        }
    }
}
