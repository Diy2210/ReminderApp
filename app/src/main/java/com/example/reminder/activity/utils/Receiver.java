package com.example.reminder.activity.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.example.reminder.R;
import com.example.reminder.activity.activity.MainActivity;
import com.example.reminder.activity.activity.RAPP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Receiver extends BroadcastReceiver {

    public Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
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
                        .setContentIntent(pendingIntent)
                        .setSound(defaultSoundUri);
        if (RAPP.vibrationSetting == 1) {
            v.vibrate(500);
        }

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
    }
}
