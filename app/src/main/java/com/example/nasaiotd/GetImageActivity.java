package com.example.nasaiotd;

import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

public class GetImageActivity extends ActivityBase {

    private static final String LOG_TAG = "NASA_MAIN";

    /**
     * An instance of ImagesAdapter that backs ImagesList.
     */
    private ImagesAdapter imagesAdapter;

    private DrawerLayout navigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);

        setupNavigation(R.string.GetImageTitle);

        final ImageDao imageDao = new ImageDao(this);
        List<ImageData> images = imageDao.load();

        imagesAdapter = new ImagesAdapter(getLayoutInflater(), R.layout.imagelist_item);
        imagesAdapter.addRange(images);

        ListView imagesList = findViewById(R.id.ImageList);
        imagesList.setAdapter(imagesAdapter);

        imagesList.setOnItemLongClickListener((list, view, position, id) -> {
            ImageData image = (ImageData)imagesAdapter.getItem(position);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder
                    .setTitle(R.string.ImageListDeleteTitle)
                    .setMessage(image.getTitle())
                    .setPositiveButton(R.string.ImageListDeleteYes, (click, arg) -> {
                        imageDao.delete(image);

                        imagesAdapter.removeAt(position);
                        imagesAdapter.notifyDataSetChanged();

                        Snackbar
                            .make(imagesList, image.getTitle() + " deleted.", Snackbar.LENGTH_SHORT)
                            .setAction(R.string.ImageListDeleteUndo, v -> {
                                imagesAdapter.insertAt(position, image);
                                imagesAdapter.notifyDataSetChanged();

                                imageDao.save(image);
                            })
                            .show();
                    })
                    .setNegativeButton(R.string.ImageListDeleteNo, (click, arg) -> {

                    })
                    .create()
                    .show();

            return true;
        });

        imagesList.setOnItemClickListener((list, view, position, id) -> {
            ImageData imageData = (ImageData) imagesAdapter.getItem(position);

            Bundle detailsBundle = new Bundle();
            detailsBundle.putLong("id", imageData.getId());

            Intent intent = new Intent(GetImageActivity.this, ImageDetailsActivity.class);
            intent.putExtras(detailsBundle);

            startActivity(intent);
        });

        final SharedPreferences preferences =
            getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE);
        final String lastDate = preferences
            .getString(getString(R.string.SavedDateKey), ImageUtils.getCurrentDateString());

        EditText dateInput = findViewById(R.id.DateInput);
        dateInput.setText(lastDate);

        Button selectDateButton = findViewById(R.id.SelectDateButton);
        selectDateButton.setOnClickListener(v -> {
            DialogFragment fragment = new DatePickerFragment(s -> {
                dateInput.setText(s);
                hideKeyboard();
            });

            Bundle dateData = new Bundle();
            dateData.putString("date", dateInput.getText().toString());
            fragment.setArguments(dateData);

            fragment.show(getSupportFragmentManager(), "datePicker");
        });

        Button fetchImageButton = findViewById(R.id.FetchImageButton);
        fetchImageButton.setOnClickListener(v -> {
            String date = dateInput.getText().toString();
            hideKeyboard();

            SharedPreferences.Editor editor =
                getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE)
                    .edit();

            editor.putString(getString(R.string.SavedDateKey), date)
                .apply();

            NasaApiQuery query = new NasaApiQuery(this, imagesAdapter);
            query.execute(date);
        });
    }

    /**
     * Hides the software keyboard.
     * https://stackoverflow.com/a/17789187
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();

        if (view == null) {
            view = new View(this);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void showHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = getString(R.string.GetImageHelpText);

        builder
                .setTitle(R.string.HelpTitle)
                .setMessage(message)
                .setPositiveButton(R.string.HelpOkay, (click, arg) -> {

                })
                .create()
                .show();
    }
}