package com.gambitdev.talktome.Interfaces;

public interface OnChatClicked extends OnContactClick {
    void onLongClicked(String uid , boolean isContact , String phoneNumber);
}
