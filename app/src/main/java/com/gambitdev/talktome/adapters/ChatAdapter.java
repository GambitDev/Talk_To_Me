package com.gambitdev.talktome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.activities.ChatActivity;
import com.gambitdev.talktome.interfaces.OnMessageClick;
import com.gambitdev.talktome.interfaces.Selectable;
import com.gambitdev.talktome.models.Message;
import com.gambitdev.talktome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter extends FirebaseRecyclerAdapter <Message , ChatAdapter.MsgViewHolder>
        implements Selectable {

    private Context context;
    private String userUid;
    private String contactUid;
    private OnMessageClick listener;

    private boolean selectionMode, clearSelection;
    private ArrayList<Integer> selectedItems = new ArrayList<>();
    private DatabaseReference reference;
    private FirebaseDatabase db;

    private static final int USER_TEXT = 0;
    private static final int CONTACT_TEXT = 1;
    private static final int USER_IMG = 2;
    private static final int CONTACT_IMG = 3;

    public ChatAdapter(FirebaseRecyclerOptions<Message> options,
                       Context context, String contactUid) {
        super(options);
        this.context = context;
        this.contactUid = contactUid;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userUid = mAuth.getCurrentUser().getUid();
            db = FirebaseDatabase.getInstance();
            reference = db.getReference()
                    .child("chats")
                    .child(userUid)
                    .child(contactUid)
                    .child("messages");
        }
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
        if (clearSelection)
            holder.selectionShade.setVisibility(View.GONE);
        holder.root.setOnLongClickListener(v -> {
            selectionMode = true;
            clearSelection = false;
            select(holder.getLayoutPosition(), holder);
            return true;
        });
        switch (getItemViewType(position)) {
            case USER_TEXT:
            case CONTACT_TEXT:
                TextMsgViewHolder textViewHolder = (TextMsgViewHolder) holder;
                textViewHolder.msgTv.setText(msg.getTxtMsg());
                textViewHolder.root.setOnClickListener(v -> {
                    if (selectionMode)
                        if (selectedItems.contains(textViewHolder.getLayoutPosition())) {
                            deselect(textViewHolder.getLayoutPosition(), textViewHolder);
                            return;
                        }
                        select(textViewHolder.getLayoutPosition(), textViewHolder);
                });
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
                imgViewHolder.root.setOnClickListener(v -> {
                    if (!selectionMode)
                        listener.onImageMessageClicked(msg.getImgUrl());
                    else if (!selectedItems.contains(imgViewHolder.getLayoutPosition()))
                        select(imgViewHolder.getLayoutPosition(), imgViewHolder);
                    else
                        deselect(imgViewHolder.getLayoutPosition(), imgViewHolder);
                });
                if (msg.getTxtMsg().isEmpty()) {
                    imgViewHolder.caption.setVisibility(View.GONE);
                } else {
                    imgViewHolder.caption.setVisibility(View.VISIBLE);
                    imgViewHolder.caption.setText(msg.getTxtMsg());
                }
                break;
        }
    }

    public void cancelSelection() {
        selectedItems = new ArrayList<>();
        selectionMode = false;
        clearSelection = true;
        notifyDataSetChanged();
    }

    public void deleteSelectedItems() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference chatRef = db.getReference()
                        .child("chats")
                        .child(userUid)
                        .child(contactUid);
                //if all msgs are selected, delete chat completely
                if (dataSnapshot.getChildrenCount() == selectedItems.size()) {
                    chatRef.removeValue();
                    ((ChatActivity) context).closeSelectionOptions();
                    reference.removeEventListener(this);
                    return;
                }
                //find new last msg after deletion
                int lastMsgPos = (int)dataSnapshot.getChildrenCount() - 1;
                int newLastMsgPos = 0;
                for (int i = lastMsgPos; i >= 0; i--) {
                    if (!selectedItems.contains(i)) {
                        newLastMsgPos = i;
                        break;
                    }
                }
                //delete selected items
                ArrayList<String> keysForDeletion = new ArrayList<>();
                int snapshotPos = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //set new last msg
                    if (snapshotPos == newLastMsgPos) {
                        Message lastMsg = child.getValue(Message.class);
                        chatRef.child("last_msg").setValue(lastMsg);
                    }
                    for (Integer selectedItemPos : selectedItems) {
                        Message msgForDeletion = getSnapshots().get(selectedItemPos);
                        Message dataMsg = child.getValue(Message.class);
                        if (dataMsg != null && msgForDeletion.isEqualTo(dataMsg)) {
                            keysForDeletion.add(child.getKey());
                        }
                    }
                    snapshotPos++;
                }
                for (String key : keysForDeletion) {
                    DatabaseReference msgRef = reference.child(key);
                    msgRef.removeValue();
                }
                ((ChatActivity) context).closeSelectionOptions();
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

    public <T extends RecyclerView.ViewHolder> void select(int position, @NonNull T holder) {
        ((MsgViewHolder) holder).selectionShade.setVisibility(View.VISIBLE);
        selectedItems.add(position);
        ((ChatActivity)((MsgViewHolder) holder).selectionShade.getContext())
                .updateSelectionOptions(selectedItems.size());
    }

    public <T extends RecyclerView.ViewHolder> void deselect(int position, @NonNull T holder) {
        ((MsgViewHolder) holder).selectionShade.setVisibility(View.GONE);
        selectedItems.remove(Integer.valueOf(position));
        ((ChatActivity)((MsgViewHolder) holder).selectionShade.getContext())
                .updateSelectionOptions(selectedItems.size());
        if (selectedItems.size() == 0)
            ((ChatActivity)((MsgViewHolder) holder).selectionShade.getContext())
                    .closeSelectionOptions();
    }

    static class MsgViewHolder extends RecyclerView.ViewHolder {

        View selectionShade;
        ConstraintLayout root;

        MsgViewHolder(@NonNull View itemView) {
            super(itemView);

            root = itemView.findViewById(R.id.root);
            selectionShade = itemView.findViewById(R.id.selection);
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
