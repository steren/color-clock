package fr.steren.colorclock;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ColorClockWallpaperSettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new ColorClockWallpaperSettingsFragment())
                .commit();
    }

}
