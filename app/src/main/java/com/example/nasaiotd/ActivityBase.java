package com.example.nasaiotd;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public abstract class ActivityBase extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "NASA_BASE";

    private DrawerLayout navigationDrawer;

    protected void setupNavigation(int titleResourceId) {
        Toolbar toolBar = findViewById(R.id.MainToolBar);
        setSupportActionBar(toolBar);

        navigationDrawer = findViewById(R.id.MainNavigationDrawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navigationDrawer,
                toolBar, R.string.NavigationOpen, R.string.NavigationClose);

        navigationDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.MainNavigation);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);

        TextView headerTitle = view.findViewById(R.id.NavigationHeaderActivityName);
        headerTitle.setText(titleResourceId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.ToolbarHome:
                Log.i(LOG_TAG, "Navigating to Home via Toolbar.");
                intent = new Intent(ActivityBase.this, HomeActivity.class);
                break;
            case R.id.ToolbarGetImage:
                Log.i(LOG_TAG, "Navigating to GetImage via Toolbar.");
                intent = new Intent(ActivityBase.this, GetImageActivity.class);
                break;
            case R.id.ToolbarGallery:
                Log.i(LOG_TAG, "Navigating to Gallery via Toolbar.");
                intent = new Intent(ActivityBase.this, GalleryActivity.class);
                break;
            case R.id.ToolbarSettings:
                Log.i(LOG_TAG, "Navigating to Settings via Toolbar.");
                intent = new Intent(ActivityBase.this, SettingsActivity.class);
                break;
            case R.id.ToolbarHelp:
                Log.i(LOG_TAG, "Showing help for: " + ActivityBase.this.getClass().getName());
                showHelp();
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    protected abstract void showHelp();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.NavigationHomeItem:
                Log.i(LOG_TAG, "Navigating to Home via NavigationView.");
                intent = new Intent(ActivityBase.this, HomeActivity.class);
                break;

            case R.id.NavigationGetImageItem:
                Log.i(LOG_TAG, "Navigating to GetImage via NavigationView.");
                intent = new Intent(ActivityBase.this, GetImageActivity.class);
                break;

            case R.id.NavigationGalleryItem:
                Log.i(LOG_TAG, "Navigating to Gallery via NavigationView.");
                intent = new Intent(ActivityBase.this, GalleryActivity.class);
                break;

            case R.id.NavigationSettingsItem:
                Log.i(LOG_TAG, "Navigating to Settings via NavigationView.");
                intent = new Intent(ActivityBase.this, SettingsActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

        navigationDrawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
            navigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
