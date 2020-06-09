package com.gambitdev.talktome.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gambitdev.talktome.Dialogs.EditStatusBottomSheet;
import com.gambitdev.talktome.Dialogs.UserImagePickerBottomSheet;
import com.gambitdev.talktome.Interfaces.OnProfileImgOptions;
import com.gambitdev.talktome.Models.User;
import com.gambitdev.talktome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class UserProfileActivity extends AppCompatActivity
        implements OnProfileImgOptions, EasyPermissions.PermissionCallbacks {

    private static final int GET_IMAGE_PERMISSION = 1;
    private static final int REQUEST_LOAD_IMG = 2;

    User user;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference().child("images");
    DatabaseReference reference;
    ValueEventListener listener;
    ImageView userImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        findViewById(R.id.back_btn).setOnClickListener(v -> finish());

        userImg = findViewById(R.id.user_img);
        userImg.setOnClickListener(v -> {
            Intent goToViewImg = new Intent(UserProfileActivity.this,
                    ViewImageActivity.class);
            goToViewImg.putExtra("img_url" , user.getProfilePicUrl());
            startActivity(goToViewImg);
        });
        TextView status = findViewById(R.id.status);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    if (user.getProfilePicUrl() != null)
                        Picasso.get().load(user.getProfilePicUrl()).into(userImg);
                    if (user.getStatus() != null)
                        status.setText(user.getStatus());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if (mAuth.getCurrentUser() != null) {
            reference = db.getReference().child("users")
                    .child(mAuth.getCurrentUser().getUid());
            reference.addValueEventListener(listener);
        }

        findViewById(R.id.edit_btn).setOnClickListener(v -> {
            EditStatusBottomSheet bottomSheet = EditStatusBottomSheet.newInstance();
            bottomSheet.setListener(this);
            bottomSheet.show(getSupportFragmentManager(),
                    "edit_status_dialog");
        });

        findViewById(R.id.change_img_btn).setOnClickListener(v -> {
            UserImagePickerBottomSheet bottomSheet = UserImagePickerBottomSheet.newInstance();
            bottomSheet.setListener(this);
            bottomSheet.show(getSupportFragmentManager(),
                    "change_img_dialog");
        });
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
                    "Access to external storage is necessary to access gallery.",
                    GET_IMAGE_PERMISSION,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
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
                Uri selectedImg = data.getData();
                findViewById(R.id.progress).setVisibility(View.VISIBLE);
                uploadImg(selectedImg);
            }
        }
    }

    private void uploadImg(Uri imgUri) {
        if (imgUri != null) {
            if (imgUri.getLastPathSegment() != null) {
                storageReference = storageReference.child(imgUri.getLastPathSegment());
                UploadTask uploadTask = storageReference.putFile(imgUri);
                uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(UserProfileActivity.this,
                                "Something went wrong",
                                Toast.LENGTH_SHORT).show();
                    }
                    return storageReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String downloadUrl;
                        if (task.getResult() != null) {
                            downloadUrl = task.getResult().toString();
                            Picasso.get().load(downloadUrl).into(userImg, new Callback() {
                                @Override
                                public void onSuccess() {
                                    findViewById(R.id.progress).setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                            user.setProfilePicUrl(downloadUrl);
                            reference.setValue(user);
                        }

                    } else {
                        Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onEditStatusBtnClicked(String status) {
        if (user != null) {
            user.setStatus(status);
            reference.setValue(user);
        }
    }

    @Override
    public void onImgFromCameraClicked() {

    }

    @Override
    public void onImgFromGalleryClicked() {
        requestGalleryImgPicker();
    }

    @Override
    public void onRemoveImgClicked() {
        userImg.setImageDrawable(getDrawable(R.drawable.profile_pic_default));
        user.setProfilePicUrl(null);
        reference.setValue(user);
    }

    @Override
    protected void onStop() {
        super.onStop();
        reference.removeEventListener(listener);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
