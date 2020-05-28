package com.gambitdev.talktome.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.Adapters.ChatAdapter;
import com.gambitdev.talktome.Interfaces.OnMessageClick;
import com.gambitdev.talktome.Pojo.Message;
import com.gambitdev.talktome.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ChatActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks, OnMessageClick {

    private FirebaseAuth mAuth;
    private DatabaseReference userChatRef;
    private DatabaseReference userLastMsgRef;
    private DatabaseReference contactChatRef;
    private DatabaseReference contactLastMsgRef;
    private ChatAdapter adapter;
    private TextInputLayout msgEt;
    private FirebaseRecyclerOptions<Message> options;
    private RecyclerView msgList;
    private LinearLayoutManager linearLayoutManager;

    private static final int GET_IMAGE_PERMISSION = 1;
    private static final int REQUEST_LOAD_IMG = 2;
    private static final int REQUEST_SEND_IMG = 3;

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
                        .child("chats").child(contactUid).child("messages");
                userLastMsgRef = usersRef.child(mAuth.getCurrentUser().getUid())
                        .child("chats").child(contactUid).child("last_msg");
                contactChatRef = usersRef.child(contactUid)
                        .child("chats").child(mAuth.getCurrentUser().getUid()).child("messages");
                contactLastMsgRef = usersRef.child(contactUid)
                        .child("chats").child(mAuth.getCurrentUser().getUid()).child("last_msg");
                options = new FirebaseRecyclerOptions.Builder<Message>()
                                .setQuery(userChatRef, snapshot -> new Message(
                                        snapshot.child("senderUid").getValue(String.class),
                                        snapshot.child("txtMsg").getValue(String.class),
                                        snapshot.child("imgUrl").getValue(String.class)))
                                .build();
            }
        }

        String contactName = getIntent().getStringExtra("contact_name");
        MaterialToolbar toolbar = findViewById(R.id.top_app_bar);
        toolbar.setTitle(contactName);

        msgList = findViewById(R.id.msg_list);
        linearLayoutManager = new LinearLayoutManager(this);
        msgList.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter(options);
        adapter.setListener(this);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                linearLayoutManager.smoothScrollToPosition(msgList ,
                        null ,
                        adapter.getItemCount());
            }
        });
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
                    newMsg = new Message(mAuth.getCurrentUser().getUid(), msg , null);
                    addMsgToDB(newMsg);
                    msgEt.getEditText().setText("");
                }
            }
        });
        msgEt.setStartIconOnClickListener(v ->
                requestImagePicker());
    }

    private void addMsgToDB(Message newMsg) {
        userChatRef.push().setValue(newMsg);
        userLastMsgRef.setValue(newMsg);
        contactChatRef.push().setValue(newMsg);
        contactLastMsgRef.setValue(newMsg);
    }

    @AfterPermissionGranted(GET_IMAGE_PERMISSION)
    private void requestImagePicker() {
        if (EasyPermissions.hasPermissions(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, REQUEST_LOAD_IMG);
        } else {
            EasyPermissions.requestPermissions(this,
                    "Access to external storage is necessary to access gallery.",
                    GET_IMAGE_PERMISSION,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_LOAD_IMG) {
                Uri selectedImgUri = data.getData();
                Intent goToSendImg = new Intent(ChatActivity.this , SendImageActivity.class);
                goToSendImg.putExtra("img" , selectedImgUri);
                startActivityForResult(goToSendImg , REQUEST_SEND_IMG);
            } else if (requestCode == REQUEST_SEND_IMG) {
                String msgJson = data.getStringExtra("msg_json");
                Message msg = new Gson().fromJson(msgJson , Message.class);
                addMsgToDB(msg);
            }
        }
    }

    public void scrollDown() {
        linearLayoutManager.smoothScrollToPosition(msgList , null , adapter.getItemCount());
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

    @Override
    public void onImageMessageClicked(String imgUrl) {
        Intent goToViewImage = new Intent(ChatActivity.this , ViewImageActivity.class);
        goToViewImage.putExtra("img_url" , imgUrl);
        startActivity(goToViewImage);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
