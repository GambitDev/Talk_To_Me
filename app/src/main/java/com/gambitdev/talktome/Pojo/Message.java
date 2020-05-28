package com.gambitdev.talktome.Pojo;

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

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getTxtMsg() {
        return txtMsg;
    }

    public void setTxtMsg(String txtMsg) {
        this.txtMsg = txtMsg;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
