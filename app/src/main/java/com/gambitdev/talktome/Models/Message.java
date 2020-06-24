package com.gambitdev.talktome.Models;

public class Message{

    private String senderUid;
    private String txtMsg;
    private String imgUrl;

    public Message() {

    }

    public Message(String senderUid, String txtMsg, String imgUrl) {
        this.senderUid = senderUid;
        this.txtMsg = txtMsg;
        this.imgUrl = imgUrl;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public String getTxtMsg() {
        return txtMsg;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
