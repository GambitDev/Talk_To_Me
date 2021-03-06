package com.gambitdev.talktome.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gambitdev.talktome.interfaces.OnImgOptions;
import com.gambitdev.talktome.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ImageMsgPickerBottomSheet extends BottomSheetDialogFragment {

    private OnImgOptions listener;

    public void setListener(OnImgOptions listener) {
        this.listener = listener;
    }

    public static ImageMsgPickerBottomSheet newInstance() {
        return new ImageMsgPickerBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.img_msg_picker_bottom_sheet , container , false);
        view.findViewById(R.id.tv_btn_add_photo_camera).setOnClickListener(v ->
                selectImgFromCamera());
        view.findViewById(R.id.tv_btn_add_photo_gallery).setOnClickListener(v ->
                selectImgFromGallery());
        return view;
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }

    private void selectImgFromCamera() {
        listener.onImgFromCameraClicked();
        dismiss();
    }

    private void selectImgFromGallery() {
        listener.onImgFromGalleryClicked();
        dismiss();
    }
}
