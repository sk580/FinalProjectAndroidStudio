package com.example.nasaiotd;

import android.util.Log;
import android.widget.ImageView;


public class SingleImageResult implements ImageDataContainer {
    private static final String LOG_TAG = "NASA_IMAGE_RESULT";

    private final OnImageChangeListener listener;
    private ImageData imageData;

    public SingleImageResult(OnImageChangeListener listener) {
        this.listener = listener;
    }

    public ImageData getImageData() {
        return imageData;
    }

    @Override
    public void add(ImageData imageData) {
        this.imageData = imageData;
    }

    @Override
    public void notifyDataSetChanged() {
        Log.i(LOG_TAG, "Image data received from API.");

        if (listener != null) {
            listener.OnImageChange(getImageData());
        }
    }

    public interface OnImageChangeListener {
        void OnImageChange(ImageData image);
    }
}
