package com.example.nasaiotd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;

public final class ImageUtils {

    public static Bitmap getImageFromLocal(Context context, String fileName) {
        Bitmap image = null;

        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            image = BitmapFactory.decodeStream(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }


    public static String getCurrentDateString() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return String.format("%d-%02d-%02d", year, month + 1, day);
    }
}
