package com.gambitdev.talktome.Models;

public class ChatListItem {

    private String uid;
    private String contactName;
    private String phoneNumber;
    private String lastMsg;
    private String profilePicUrl;
    private boolean inPhoneContacts;

    public ChatListItem(String uid, String contactName, String phoneNumber,
                        String lastMsg, String profilePicUrl, boolean inPhoneContacts) {
        this.uid = uid;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.lastMsg = lastMsg;
        this.profilePicUrl = profilePicUrl;
        this.inPhoneContacts = inPhoneContacts;
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

    public boolean isInPhoneContacts() {
        return inPhoneContacts;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
