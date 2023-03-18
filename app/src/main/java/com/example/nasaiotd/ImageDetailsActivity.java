package com.example.nasaiotd;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ImageDetailsActivity extends ActivityBase {

    private static final String LOG_TAG = "IMAGE_DETAILS_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        setupNavigation(R.string.DetailsTitle);

        Bundle data = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();

        ImageDetailsFragment detailsFragment = new ImageDetailsFragment();
        detailsFragment.setArguments(data);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.DetailsFrame, detailsFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void showHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = getString(R.string.ImageDetailsHelpText);

        builder
            .setTitle(R.string.HelpTitle)
            .setMessage(message)
            .setPositiveButton(R.string.HelpOkay, (click, arg) -> {

            })
            .create()
            .show();
    }
}