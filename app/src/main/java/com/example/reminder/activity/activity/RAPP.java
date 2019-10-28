package com.example.reminder.activity.activity;

import android.app.Application;

public class RAPP extends Application {

    public static final int NOTIFICATION_REMINDER = 100;
    public static final int NOTIFICATION_INTENT = 200;
    public static final String NOTIFICATION_ID = "notification_id";

    public static int reminder_id = 0;
    public static int vibrationSetting = 1;
    public static int soundSetting = 1;
    public static int hourNotification = 0, minuteNotification = 0;
    public static int repeatStatus = 0;
    public static long millisNotification = 0;
    public static String titleNotification = "";


}
