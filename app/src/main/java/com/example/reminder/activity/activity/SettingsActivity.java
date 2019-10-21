package com.example.reminder.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.reminder.R;

public class SettingsActivity extends AppCompatActivity {

    private Switch soundSBtn, vibrationSBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        soundSBtn = findViewById(R.id.soundSBtn);
        if (RAPP.soundSetting == 1) {
            soundSBtn.setChecked(true);
        }
        vibrationSBtn = findViewById(R.id.vibrationSBtn);
        if (RAPP.vibrationSetting == 1) {
            vibrationSBtn.setChecked(true);
        }

        soundSBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    RAPP.soundSetting = 1;
                } else {
                    RAPP.soundSetting = 0;
                }
            }
        });

        vibrationSBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    RAPP.vibrationSetting = 1;
                } else {
                    RAPP.vibrationSetting = 0;
                }
            }
        });
    }
}
