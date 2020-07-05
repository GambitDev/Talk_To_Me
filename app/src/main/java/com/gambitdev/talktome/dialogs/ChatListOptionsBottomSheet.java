package com.gambitdev.talktome.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gambitdev.talktome.interfaces.OnChatListOptions;
import com.gambitdev.talktome.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChatListOptionsBottomSheet extends BottomSheetDialogFragment {

    private OnChatListOptions listener;
    private String uid , phoneNumber;
    private boolean isContact;

    private ChatListOptionsBottomSheet(String uid , boolean isContact , String phoneNumber) {
        this.uid = uid;
        this.isContact = isContact;
        this.phoneNumber = phoneNumber;
    }

    public void setListener(OnChatListOptions listener) {
        this.listener = listener;
    }

    public static ChatListOptionsBottomSheet newInstance(String uid,
                                                         boolean isContact,
                                                         String phoneNumber) {
        return new ChatListOptionsBottomSheet(uid , isContact , phoneNumber);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_list_menu_bottom_sheet, container , false);
        if (!isContact) {
            view.findViewById(R.id.tv_add_contact).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tv_add_contact).setOnClickListener(v -> {
                listener.addContact(phoneNumber);
                dismiss();
            });
        }
        view.findViewById(R.id.tv_btn_delete_chat).setOnClickListener(v -> {
            view.findViewById(R.id.delete_conformation_tv).setVisibility(View.VISIBLE);
            view.findViewById(R.id.btn_container).setVisibility(View.VISIBLE);
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
