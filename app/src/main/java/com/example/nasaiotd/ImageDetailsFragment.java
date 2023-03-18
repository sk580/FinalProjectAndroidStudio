package com.example.nasaiotd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;

public class ImageDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_details, container, false);

        Bundle data = getArguments();

        long id = data.getLong("id");

        final ImageDao imageDao = new ImageDao(view.getContext());
        ImageData imageData = imageDao.get(id);

        if (imageData.getImage() != null) {
            ImageView image = view.findViewById(R.id.ImageDetailsImage);
            Bitmap bitmap = imageData.getImage();
            image.setImageBitmap(bitmap);
        }

        TextView dateText = view.findViewById(R.id.ImageDetailsDate);
        dateText.setText(imageData.getDate());

        TextView titleText = view.findViewById(R.id.ImageDetailsTitle);
        titleText.setText(imageData.getTitle());

        TextView urlText = view.findViewById(R.id.ImageDetailsUrl);
        urlText.setText(imageData.getUrl());

        urlText.setOnClickListener(v -> {
            openInBrowser(imageData.getUrl());
        });

        TextView hdUrlText = view.findViewById(R.id.ImageDetailsHdUrl);
        hdUrlText.setText(imageData.getHdUrl());

        if (imageData.getHdUrl() != null && imageData.getHdUrl().length() > 0) {
            hdUrlText.setOnClickListener(v -> {
                openInBrowser(imageData.getHdUrl());
            });

            hdUrlText.setVisibility(View.VISIBLE);
        }

        TextView explanationText = view.findViewById(R.id.ImageDetailsExplanation);
        explanationText.setText(imageData.getExplanation());

        return view;
    }

    private void openInBrowser(String urlString) {
        Uri uri = Uri.parse(urlString);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(getView().getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}