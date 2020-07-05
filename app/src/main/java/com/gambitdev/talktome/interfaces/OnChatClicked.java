package com.gambitdev.talktome.interfaces;

public interface OnChatClicked extends OnContactClick {
    void onLongClicked(String uid , boolean isContact , String phoneNumber);
}
