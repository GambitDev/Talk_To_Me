package com.gambitdev.talktome.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gambitdev.talktome.Interfaces.OnProfileDialogOptionClicked;
import com.gambitdev.talktome.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class UserImagePickerBottomSheet extends BottomSheetDialogFragment {

    private OnProfileDialogOptionClicked listener;

    public void setListener(OnProfileDialogOptionClicked listener) {
        this.listener = listener;
    }

    public static UserImagePickerBottomSheet newInstance() {
        return new UserImagePickerBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_img_picker_bottom_sheet , container , false);
        view.findViewById(R.id.tv_btn_add_photo_camera).setOnClickListener(v ->
                selectImgFromCamera());
        view.findViewById(R.id.tv_btn_add_photo_gallery).setOnClickListener(v ->
                selectImgFromGallery());
        view.findViewById(R.id.tv_btn_remove_photo).setOnClickListener(v ->
                removeUserImg());
        return view;
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }

    private void selectImgFromCamera() {
        listener.onImgFromCameraClicked();
    }

    private void selectImgFromGallery() {
        listener.onImgFromGalleryClicked();
    }

    private void removeUserImg() {
        listener.onRemoveImgClicked();
    }
}
