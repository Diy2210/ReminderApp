package com.example.reminder.activity.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.reminder.R;

public class FragmentSettings extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);
    }
}