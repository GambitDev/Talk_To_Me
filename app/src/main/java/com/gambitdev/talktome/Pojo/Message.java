package com.gambitdev.talktome.Pojo;

import android.graphics.Bitmap;

import androidx.room.Entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Message{

    private String timestamp;
    private String senderUid;
    private String txtMsg;
    private Bitmap imgMsg;

    public Message() {

    }

    public Message(String timestamp, String senderUid, String txtMsg, Bitmap imgMsg) {
        this.timestamp = timestamp;
        this.senderUid = senderUid;
        this.txtMsg = txtMsg;
        this.imgMsg = imgMsg;
    }

    public Message(String txtMsg , String senderUid) {
        this(senderUid);
        this.txtMsg = txtMsg;
    }

    public Message(Bitmap imgMsg , String senderUid) {
        this(senderUid);
        this.imgMsg = imgMsg;
    }

    private Message(String senderUid) {
        timestamp = formatTimestamp();
        this.senderUid = senderUid;
    }

    private String formatTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm",
                Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getTxtMsg() {
        return txtMsg;
    }

    public void setTxtMsg(String txtMsg) {
        this.txtMsg = txtMsg;
    }

    public Bitmap getImgMsg() {
        return imgMsg;
    }

    public void setImgMsg(Bitmap imgMsg) {
        this.imgMsg = imgMsg;
    }
}
