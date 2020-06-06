package com.gambitdev.talktome.Models;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {

    @NonNull
    @PrimaryKey
    private String uid;

    private String name;
    private String phoneNumber;
    private String profilePicUrl;
    private String status;

    public Contact(@NonNull String uid,
                   String name,
                   String phoneNumber,
                   String profilePicUrl,
                   String status) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePicUrl = profilePicUrl;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
