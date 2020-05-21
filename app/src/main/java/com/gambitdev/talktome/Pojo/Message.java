package com.gambitdev.talktome.Pojo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Message {

    private String timestamp;

    public Message() {
        timestamp = getTimestamp();
    }

    private String getTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm",
                Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
