package com.example.nasaiotd;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.MimeTypeMap;


public class ImageData {

    private long id;
    private String date;
    private String title;
    private String explanation;
    private String url;
    private Bitmap image;
    private String hdUrl;

    public ImageData(String date, String title, String explanation, String url, String hdUrl) {
        this.date = date;
        this.title = title;
        this.explanation = explanation;
        this.url = url;
        this.hdUrl = hdUrl;
    }

    public ImageData(long id, String date, String title, String explanation, String url, String hdUrl) {
        this(date, title, explanation, url, hdUrl);

        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getHdUrl() {
        return hdUrl;
    }

    public String getFileName() {
        String url = getUrl();
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        return getDate() + "." + extension;
    }
}
