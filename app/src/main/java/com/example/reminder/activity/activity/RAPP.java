package com.example.reminder.activity.activity;

import android.app.Application;
import android.net.Uri;

public class RAPP extends Application {

    public static final int NOTIFICATION_REMINDER = 100;
    public static final int NOTIFICATION_INTENT = 200;
    public static final String NOTIFICATION_ID = "notification_id";

    public static String titleNotification = "";
    public static int reminder_id = 0;
    public static boolean vibrationSetting = true;
    public static boolean soundSetting = true;
    public static Uri uriSetting;
    public static int hourNotification = 0, minuteNotification = 0;
    public static int repeatStatus = 0;
    public static long intervalRepeatMilliseconds = 0;
    public static long millisNotification = 0;
}
