package com.gambitdev.talktome.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.Adapters.ChatAdapter;
import com.gambitdev.talktome.Pojo.Message;
import com.gambitdev.talktome.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userChatRef;
    private DatabaseReference userLastMsgRef;
    private DatabaseReference contactChatRef;
    private DatabaseReference contactLastMsgRef;
    private ChatAdapter adapter;
    private TextInputLayout msgEt;

    private FirebaseRecyclerOptions<Message> options;

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
                options = new FirebaseRecyclerOptions.Builder<Message>()
                                .setQuery(userChatRef, snapshot -> new Message(snapshot.child("timestamp").getValue(String.class),
                                        snapshot.child("senderUid").getValue(String.class),
                                        snapshot.child("txtMsg").getValue(String.class),
                                        snapshot.child("imgMsg").getValue(Bitmap.class)))
                                .build();
            }
        }

        String contactName = getIntent().getStringExtra("contact_name");
        MaterialToolbar toolbar = findViewById(R.id.top_app_bar);
        toolbar.setTitle(contactName);

        RecyclerView msgList = findViewById(R.id.msg_list);
        msgList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(options);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
