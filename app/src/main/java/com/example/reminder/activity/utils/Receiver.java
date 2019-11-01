package com.example.reminder.activity.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        DateFormat simple = new SimpleDateFormat("HH:mm");
        Date result = new Date(RAPP.millisNotification);
        String time = simple.format(result);

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, RAPP.NOTIFICATION_INTENT, i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, RAPP.NOTIFICATION_ID)
                        .setSmallIcon(R.drawable.ic_alert)
                        .setContentTitle(RAPP.titleNotification)
                        .setContentText(time)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ("Reminder");
            String description = (RAPP.titleNotification);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(RAPP.NOTIFICATION_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            // 0 - ID of notification
            notificationManager.notify(0, notificationBuilder.build());
        }

        playSound(context);
        startVibrate();
    }

    private void playSound(Context context) {
        if (RAPP.soundSetting) {
            mediaPlayer = new MediaPlayer();
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
    }

    private void startVibrate() {
        if (RAPP.vibrationSetting) {
            vibrator.vibrate(500);
        }
    }
}
