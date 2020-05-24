package com.gambitdev.talktome.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.gambitdev.talktome.Adapters.ChatAdapter;
import com.gambitdev.talktome.Pojo.Message;
import com.gambitdev.talktome.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userChatRef;
    private DatabaseReference userLastMsgRef;
    private DatabaseReference contactChatRef;
    private DatabaseReference contactLastMsgRef;
    private RecyclerView msgList;
    private ChatAdapter adapter;
    private TextInputLayout msgEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeActivity();
    }

    private void initializeActivity() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = db.getReference().child("users");

        String contactUid = getIntent().getStringExtra("contact_uid");
        if (contactUid != null) {
            if (mAuth.getCurrentUser() != null) {
                userChatRef = usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("chats").child("messages").child(contactUid);
                contactChatRef = usersRef.child(contactUid)
                        .child("chats").child("messages").child(mAuth.getCurrentUser().getUid());
            }
        }

        String contactName = getIntent().getStringExtra("contact_name");
        MaterialToolbar toolbar = findViewById(R.id.top_app_bar);
        toolbar.setTitle(contactName);

        msgList = findViewById(R.id.msg_list);
        msgList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter();
        msgList.setAdapter(adapter);

        msgEt = findViewById(R.id.msg_et);

        attachListeners();
    }

    private void attachListeners() {
        msgEt.setEndIconOnClickListener(v -> {
            String msg;
            if (msgEt.getEditText() != null) {
                msg = msgEt.getEditText().getText().toString();
                if (msg.isEmpty()) return;
                Message newMsg;
                if (mAuth.getCurrentUser() != null) {
                    newMsg = new Message(msg, mAuth.getCurrentUser().getUid());
                    userChatRef.push().setValue(newMsg);
                    contactChatRef.push().setValue(newMsg);
                    msgEt.getEditText().setText("");
                }
            }
        });

        userChatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message msg = dataSnapshot.getValue(Message.class);
                if (!adapter.isMsgListInitialized())
                    adapter.setMessages(new ArrayList<>());
                adapter.addMsg(msg);
                if (adapter.getItemCount() > 0)
                    msgList.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
