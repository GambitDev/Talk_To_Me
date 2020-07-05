package com.gambitdev.talktome.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gambitdev.talktome.interfaces.OnChatOptions;
import com.gambitdev.talktome.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChatOptionsBottomSheet extends BottomSheetDialogFragment {

    private OnChatOptions listener;
    private String uid;

    private ChatOptionsBottomSheet(String uid) {
        this.uid = uid;
    }

    public void setListener(OnChatOptions listener) {
        this.listener = listener;
    }

    public static ChatOptionsBottomSheet newInstance(String uid) {
        return new ChatOptionsBottomSheet(uid);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_menu_bottom_sheet, container , false);
        view.findViewById(R.id.tv_btn_delete_chat).setOnClickListener(v -> {
            view.findViewById(R.id.delete_conformation_tv).setVisibility(View.VISIBLE);
            view.findViewById(R.id.btn_container).setVisibility(View.VISIBLE);
        });
        view.findViewById(R.id.tv_btn_show_contact).setOnClickListener(v -> {
            listener.showContact(uid);
            dismiss();
        });
        view.findViewById(R.id.tv_btn_gallery).setOnClickListener(v -> {
            listener.goToGallery(uid);
            dismiss();
        });
        view.findViewById(R.id.delete_btn).setOnClickListener(v -> {
            listener.deleteChat(uid);
            dismiss();
        });
        view.findViewById(R.id.cancel_btn).setOnClickListener(v -> {
            view.findViewById(R.id.delete_conformation_tv).setVisibility(View.GONE);
            view.findViewById(R.id.btn_container).setVisibility(View.GONE);
        });
        return view;
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }
}
