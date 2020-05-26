package com.gambitdev.talktome.Pojo;

import android.graphics.Bitmap;

public class ChatListItem {

    private String uid;
    private String contactName;
    private String lastMsg;
    private Bitmap profilePic;

    public ChatListItem(String uid, String contactName, String lastMsg, Bitmap profilePic) {
        this.uid = uid;
        this.contactName = contactName;
        this.lastMsg = lastMsg;
        this.profilePic = profilePic;
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

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }
}
