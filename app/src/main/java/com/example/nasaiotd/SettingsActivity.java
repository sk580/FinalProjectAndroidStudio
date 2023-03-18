package com.example.nasaiotd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.io.File;

public class SettingsActivity extends ActivityBase {

    private static final String[] IMAGE_TYPES = new String[] { "png", "jpg", "jpeg", "gif" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupNavigation(R.string.SettingsTitle);

        final SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE);

        final SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        final CheckBox autoDownloadTodayCheck = findViewById(R.id.SettingsAutoDownloadToday);
        boolean autoDownload = sharedPreferences.getBoolean(getString(R.string.AutoDownloadKey), true);
        autoDownloadTodayCheck.setChecked(autoDownload);

        autoDownloadTodayCheck.setOnCheckedChangeListener((view, isChecked) -> {
            preferencesEditor.putBoolean(getString(R.string.AutoDownloadKey), isChecked);
            preferencesEditor.apply();
        });

        final Button deleteCachedImagesButton = findViewById(R.id.SettingsDeleteCachedImages);
        deleteCachedImagesButton.setOnClickListener(this::DeleteCachedImagesButtonClicked);

        final Button deleteSavedDataButton = findViewById(R.id.SettingsDeleteSavedData);
        deleteSavedDataButton.setOnClickListener(v -> {
            DeleteCachedImagesButtonClicked(deleteCachedImagesButton);
            deleteDatabase(ImageDao.DATABASE_NAME);
        });
    }

    private void DeleteCachedImagesButtonClicked(View view) {
        File directory = getFilesDir();

        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());

                for (String extension : IMAGE_TYPES) {
                    if (fileExtension.equalsIgnoreCase(extension)) {
                        file.delete();
                    }
                }
            }
        }
    }

    @Override
    protected void showHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = getString(R.string.SettingsHelpText);

        builder
            .setTitle(R.string.HelpTitle)
            .setMessage(message)
            .setPositiveButton(R.string.HelpOkay, (click, arg) -> {

            })
            .create()
            .show();
    }
}