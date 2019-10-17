package com.example.reminder.activity.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.reminder.R;

public class Receiver extends BroadcastReceiver {

    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_ID = "notification_id";

    public Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "ALARM!!!", Toast.LENGTH_SHORT).show();

//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = intent.getParcelableExtra(NOTIFICATION);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_ID, "NOTIFICATION_CHANNEL_NAME", importance);
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
//        assert notificationManager != null;
//        notificationManager.notify(id, notification);

//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(context, NOTIFICATION)
//                        .setSmallIcon(R.drawable.ic_alert)
//                        .setContentTitle("Title")
//                        .setContentText("Description")
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri);
////                            .setContentIntent(this);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = ("Title");
//            String description = ("Description");
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(NOTIFICATION, name, importance);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = (NotificationManager) Context.getSystemService(context);
//            notificationManager.createNotificationChannel(channel);
//
//            // 0 - ID of notification
//            notificationManager.notify(0, notificationBuilder.build());
//        }
    }
}
