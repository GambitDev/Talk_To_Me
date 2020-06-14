package com.gambitdev.talktome.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.Adapters.ChatAdapter;
import com.gambitdev.talktome.Dialogs.ChatOptionsBottomSheet;
import com.gambitdev.talktome.Dialogs.ImageMsgPickerBottomSheet;
import com.gambitdev.talktome.Interfaces.OnChatOptions;
import com.gambitdev.talktome.Interfaces.OnImgOptions;
import com.gambitdev.talktome.Interfaces.OnMessageClick;
import com.gambitdev.talktome.Models.Message;
import com.gambitdev.talktome.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ChatActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks,
        OnMessageClick,
        OnImgOptions,
        OnChatOptions {

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
    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    private static final int GET_IMAGE_PERMISSION = 1;
    private static final int REQUEST_LOAD_IMG = 2;
    private static final int REQUEST_SEND_IMG = 3;
    private static final int GET_CAMERA_PERMISSION = 4;
    private static final int REQUEST_IMAGE_CAPTURE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeActivity();
    }

    private void initializeActivity() {
        mAuth = FirebaseAuth.getInstance();
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
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.more) {
                ChatOptionsBottomSheet bottomSheet = ChatOptionsBottomSheet
                        .newInstance(contactUid);
                bottomSheet.setListener(this);
                bottomSheet.show(getSupportFragmentManager() , "chat_options_dialog");
                return true;
            }
            return false;
        });
        toolbar.setNavigationOnClickListener(v -> finish());

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
        msgEt.setStartIconOnClickListener(v ->{
            ImageMsgPickerBottomSheet bottomSheet = ImageMsgPickerBottomSheet.newInstance();
            bottomSheet.setListener(this);
            bottomSheet.show(getSupportFragmentManager() , "img_picker_dialog");
        });
    }

    private void addMsgToDB(Message newMsg) {
        userChatRef.push().setValue(newMsg);
        userLastMsgRef.setValue(newMsg);
        contactChatRef.push().setValue(newMsg);
        contactLastMsgRef.setValue(newMsg);
    }

    @AfterPermissionGranted(GET_IMAGE_PERMISSION)
    private void requestGalleryImgPicker() {
        if (EasyPermissions.hasPermissions(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, REQUEST_LOAD_IMG);
        } else {
            EasyPermissions.requestPermissions(this,
                    getResources().getString(R.string.storage_permission_rational),
                    GET_IMAGE_PERMISSION,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @AfterPermissionGranted(GET_CAMERA_PERMISSION)
    private void requestCamera() {
        if (EasyPermissions.hasPermissions(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }
                if (photoFile != null) {
                    Uri imgUri = FileProvider.getUriForFile(this,
                            "com.gambitdev.talktome.provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } else {
            EasyPermissions.requestPermissions(this,
                    getResources().getString(R.string.storage_permission_rational),
                    GET_CAMERA_PERMISSION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode , permissions , grantResults , this);
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
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Uri selectedImgUri = Uri.parse(mCurrentPhotoPath);
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

    @Override
    public void onImgFromCameraClicked() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(this,
                    getResources().getString(R.string.no_camera_on_device),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        requestCamera();
    }

    @Override
    public void onImgFromGalleryClicked() {
        requestGalleryImgPicker();
    }

    @Override
    public void showContact(String uid) {
        Intent goToContactProfile = new Intent(ChatActivity.this,
                ContactProfileActivity.class);
        goToContactProfile.putExtra("contact_uid" , uid);
        startActivity(goToContactProfile);
    }

    @Override
    public void deleteChat(String uid) {
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference userChatRef = db.getReference()
                    .child("users")
                    .child(mAuth.getCurrentUser().getUid())
                    .child("chats")
                    .child(uid);
            userChatRef.removeValue();
            finish();
        }
    }
}
