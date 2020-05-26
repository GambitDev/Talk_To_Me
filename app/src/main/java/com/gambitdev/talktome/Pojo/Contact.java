package com.gambitdev.talktome.Pojo;

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
    private Bitmap profilePic;

    public Contact(@NonNull String uid , String name, String phoneNumber, Bitmap profilePic) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic;
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

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
