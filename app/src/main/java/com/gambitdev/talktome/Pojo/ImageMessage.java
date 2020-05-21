package com.gambitdev.talktome.Pojo;

import android.graphics.Bitmap;

public class ImageMessage extends Message {

    private Bitmap img;

    public ImageMessage(Bitmap img) {
        this.img = img;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
