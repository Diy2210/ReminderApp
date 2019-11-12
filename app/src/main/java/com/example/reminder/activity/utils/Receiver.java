package com.example.reminder.activity.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import com.example.reminder.R;
import com.example.reminder.activity.activity.MainActivity;
import com.example.reminder.activity.activity.RAPP;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Receiver extends BroadcastReceiver {

    private SharedPreferences getSound, getAlarms, getVibration;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private String repeat = "";

    public Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mediaPlayer = new MediaPlayer();
        getAlarms = PreferenceManager.getDefaultSharedPreferences(context);
        String alarms = getAlarms.getString("setting_notification", "default-ringtone");
        RAPP.uriSetting = Uri.parse(alarms);

        getSound = PreferenceManager.getDefaultSharedPreferences(context);
        RAPP.soundSetting = getSound.getBoolean("setting_sound", true);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        getVibration = PreferenceManager.getDefaultSharedPreferences(context);
        RAPP.vibrationSetting = getVibration.getBoolean("setting_vibration", true);

        // Date format for time reminder
        DateFormat simple = new SimpleDateFormat("HH:mm");
        Date result = new Date(RAPP.millisNotification);
        String time = simple.format(result);

        if (RAPP.intervalRepeatMilliseconds == 60000) {
            repeat = context.getString(R.string.repeat_after_1);
        }else if (RAPP.intervalRepeatMilliseconds == 300000) {
            repeat = context.getString(R.string.repeat_after_5);
        } else if (RAPP.intervalRepeatMilliseconds == 600000) {
            repeat = context.getString(R.string.repeat_after_10);
        } else if (RAPP.intervalRepeatMilliseconds == 900000) {
            repeat = context.getString(R.string.repeat_after_15);
        } else if (RAPP.intervalRepeatMilliseconds == 1200000) {
            repeat = context.getString(R.string.repeat_after_20);
        } else if (RAPP.intervalRepeatMilliseconds == 1800000) {
            repeat = context.getString(R.string.repeat_after_30);
        } else if (RAPP.intervalRepeatMilliseconds == 3600000) {
            repeat = context.getString(R.string.repeat_after_hour);
        } else {
            repeat = context.getString(R.string.repeat_once_day);
        }

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, RAPP.NOTIFICATION_INTENT, i,
                PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, RAPP.NOTIFICATION_ID)
                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                    .setSmallIcon(R.drawable.ic_alert)
                    .setContentTitle(RAPP.titleNotification)
                    .setContentText(time)
                    .setSubText(repeat)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

        if (RAPP.soundSetting) {
            try {
                mediaPlayer.setDataSource(context, RAPP.uriSetting);
                final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getString(R.string.app_name);
                String description = (RAPP.titleNotification);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(RAPP.NOTIFICATION_ID, name, importance);
                channel.setDescription(description);

                if (RAPP.soundSetting) {
                    if (RAPP.uriSetting != null) {
                        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .build();
                        channel.setSound(RAPP.uriSetting, audioAttributes);
                    }
                }

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);

                // 0 - ID of notification
                notificationManager.notify(0, notificationBuilder.build());
            }

            startVibrate();
        }

    private void startVibrate() {
        if (RAPP.vibrationSetting) {
            vibrator.vibrate(500);
        }
    }
}
