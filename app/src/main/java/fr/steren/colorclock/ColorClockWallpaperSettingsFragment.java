package fr.steren.colorclock;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class ColorClockWallpaperSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.wallpaper_settings, rootKey);
    }

}
