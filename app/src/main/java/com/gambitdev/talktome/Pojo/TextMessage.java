package com.gambitdev.talktome.Pojo;

public class TextMessage extends Message {

    private String msg;

    public TextMessage(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
