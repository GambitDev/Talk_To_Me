package com.gambitdev.talktome.DataManager;

import android.graphics.Bitmap;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class Converters {

    @TypeConverter
    public static String bitmapToString(Bitmap bitmap) {
        return new Gson().toJson(bitmap);
    }

    @TypeConverter
    public static Bitmap stringToBitmap(String str) {
        return new Gson().fromJson(str , Bitmap.class);
    }
}
