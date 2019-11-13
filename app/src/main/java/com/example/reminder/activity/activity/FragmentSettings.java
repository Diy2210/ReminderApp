package com.example.reminder.activity.activity;

import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.reminder.R;

public class FragmentSettings extends PreferenceFragment {

    private SwitchPreference cacheWithWifi;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        cacheWithWifi = (SwitchPreference) findPreference("setting_theme");

        if (Build.VERSION.SDK_INT >= 29) {
            preferenceScreen.addPreference(cacheWithWifi);
        } else {
            preferenceScreen.removePreference(cacheWithWifi);
        }

        cacheWithWifi.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (cacheWithWifi.isChecked()) {
                    cacheWithWifi.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    cacheWithWifi.setChecked(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                return false;
            }
        });
    }
}