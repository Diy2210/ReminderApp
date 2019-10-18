package com.example.reminder.activity.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.example.reminder.R;
import com.example.reminder.activity.activity.RAPP;

public class Receiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification_id";

    public Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, NOTIFICATION_ID)
                        .setSmallIcon(R.drawable.ic_alert)
                        .setContentTitle("Reminder")
                        .setContentText(RAPP.titleNotification)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);
        if (RAPP.vibrationSetting == 1) {
            v.vibrate(500);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ("Reminder");
            String description = (RAPP.titleNotification);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            // 0 - ID of notification
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
