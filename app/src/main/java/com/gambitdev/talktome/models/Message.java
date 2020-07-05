package com.gambitdev.talktome.models;

public class Message {

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

    public boolean isEqualTo(Message msg) {
        if (msg.imgUrl != null & imgUrl != null) {
            if (msg.imgUrl.equals(imgUrl))
                return true;
        }
        if (msg.txtMsg != null && txtMsg != null) {
            if (!msg.txtMsg.isEmpty() && !txtMsg.isEmpty())
                return msg.txtMsg.equals(txtMsg);
        }
        return false;
    }
}
