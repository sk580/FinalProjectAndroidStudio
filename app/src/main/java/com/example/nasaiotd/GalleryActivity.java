package com.example.nasaiotd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class GalleryActivity extends ActivityBase {

    private ImagesAdapter imagesAdapter;

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        FrameLayout detailsFrame = findViewById(R.id.DetailsFrame);
        isTablet = (detailsFrame != null);

        FragmentManager fragmentManager = getSupportFragmentManager();

        setupNavigation(R.string.GalleryTitle);

        final ImageDao imagesDao = new ImageDao(this);
        List<ImageData> images = imagesDao.load();

        imagesAdapter = new ImagesAdapter(getLayoutInflater(), R.layout.gallery_item);
        imagesAdapter.addRange(images);

        GridView galleryList = findViewById(R.id.GalleryGrid);
        galleryList.setAdapter(imagesAdapter);

        galleryList.setOnItemClickListener((list, view, position, id) -> {
            ImageData imageData = (ImageData) imagesAdapter.getItem(position);

            Bundle detailsBundle = new Bundle();
            detailsBundle.putLong("id", imageData.getId());

            if (isTablet) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                ImageDetailsFragment detailsFragment = new ImageDetailsFragment();
                detailsFragment.setArguments(detailsBundle);

                transaction.replace(R.id.DetailsFrame, detailsFragment);
                transaction.commit();
            } else {
                Intent intent = new Intent(GalleryActivity.this, ImageDetailsActivity.class);
                intent.putExtras(detailsBundle);

                startActivity(intent);
            }
        });

        galleryList.setOnItemLongClickListener((list, view, position, id) -> {
            ImageData image = (ImageData)imagesAdapter.getItem(position);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder
                    .setTitle(R.string.ImageListDeleteTitle)
                    .setMessage(image.getTitle())
                    .setPositiveButton(R.string.ImageListDeleteYes, (click, arg) -> {
                        imagesDao.delete(image);

                        imagesAdapter.removeAt(position);
                        imagesAdapter.notifyDataSetChanged();

                        Snackbar
                                .make(galleryList, image.getTitle() + " deleted.", Snackbar.LENGTH_SHORT)
                                .setAction(R.string.ImageListDeleteUndo, v -> {
                                    imagesAdapter.insertAt(position, image);
                                    imagesAdapter.notifyDataSetChanged();

                                    imagesDao.save(image);
                                })
                                .show();
                    })
                    .setNegativeButton(R.string.ImageListDeleteNo, (click, arg) -> {

                    })
                    .create()
                    .show();

            return true;
        });
    }

    @Override
    protected void showHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = getString(R.string.GalleryHelpText);

        builder
            .setTitle(R.string.HelpTitle)
            .setMessage(message)
            .setPositiveButton(R.string.HelpOkay, (click, arg) -> {

            })
            .create()
            .show();
    }
}