package com.gambitdev.talktome.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gambitdev.talktome.Dialogs.EditStatusBottomSheet;
import com.gambitdev.talktome.Dialogs.UserImagePickerBottomSheet;
import com.gambitdev.talktome.Interfaces.OnProfileDialogOptionClicked;
import com.gambitdev.talktome.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity implements OnProfileDialogOptionClicked {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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

    @Override
    public void onEditStatusBtnClicked(String status) {

    }

    @Override
    public void onImgFromCameraClicked() {

    }

    @Override
    public void onImgFromGalleryClicked() {

    }

    @Override
    public void onRemoveImgClicked() {

    }
}
