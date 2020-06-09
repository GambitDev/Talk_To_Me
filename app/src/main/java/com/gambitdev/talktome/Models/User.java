package com.gambitdev.talktome.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @NonNull
    @PrimaryKey
    private String uid;

    private String phoneNumber;
    private String profilePicUrl;
    private String status;

    @Ignore
    public User() {

    }

    public User(@NonNull String uid, String phoneNumber,
                String profilePicUrl, String status) {
        this.uid = uid;
        this.phoneNumber = phoneNumber;
        this.profilePicUrl = profilePicUrl;
        this.status = status;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
