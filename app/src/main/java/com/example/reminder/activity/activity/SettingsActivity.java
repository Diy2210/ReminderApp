package com.example.reminder.activity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.reminder.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction()
                .replace(R.id.settings, new FragmentSettings())
                .commit();
    }
}
