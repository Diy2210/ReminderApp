package com.example.reminder.activity.database.model;

public class Reminder {

    public static final String TABLE_NAME = "reminder";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_REMINDER_TIME = "time";
    public static final String COLUMN_REMINDER_TITLE = "title";

    private int id;
    private String time;
    private String title;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_REMINDER_TIME + " TEXT,"
                    + COLUMN_REMINDER_TITLE + " TEXT"
                    + ")";

    public Reminder() {
    }

    public Reminder(int id, String time, String title) {
        this.id = id;
        this.time = time;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReminder() {
        return title;
    }

    public void setReminder(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
