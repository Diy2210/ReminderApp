package com.example.reminder.activity.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.reminder.R;
import com.example.reminder.activity.activity.RAPP;

public class Receiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification_id";

    public Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, NOTIFICATION_ID)
                        .setSmallIcon(R.drawable.ic_alert)
                        .setContentTitle("Reminder")
                        .setContentText(RAPP.titleNotification)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);
//                         .setContentIntent(intent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ("Title");
            String description = ("Text");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            // 0 - ID of notification
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
