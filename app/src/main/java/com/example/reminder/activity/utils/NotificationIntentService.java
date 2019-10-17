package com.example.reminder.activity.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationManagerCompat;

import com.example.reminder.R;
import com.example.reminder.activity.activity.MainActivity;

public class NotificationIntentService extends JobIntentService {
    private static final int NOTIFICATION_ID = 3;

    public NotificationIntentService() {
        super();
    }

    @Override
    protected void onHandleWork(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("My Title");
        builder.setContentText("This is the Body");
        builder.setSmallIcon(R.drawable.ic_alert);
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }

    //    @Override
//    protected void onHandleIntent(Intent intent) {
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentTitle("My Title");
//        builder.setContentText("This is the Body");
//        builder.setSmallIcon(R.drawable.ic_alert);
//        Intent notifyIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        Notification notificationCompat = builder.build();
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
//    }
}
