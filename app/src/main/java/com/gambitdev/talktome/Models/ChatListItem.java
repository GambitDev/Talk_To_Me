package com.gambitdev.talktome.Models;

import android.graphics.Bitmap;

public class ChatListItem {

    private String uid;
    private String contactName;
    private String lastMsg;
    private String profilePicUrl;

    public ChatListItem(String uid, String contactName, String lastMsg, String profilePicUrl) {
        this.uid = uid;
        this.contactName = contactName;
        this.lastMsg = lastMsg;
        this.profilePicUrl = profilePicUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
