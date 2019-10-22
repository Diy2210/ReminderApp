package com.example.reminder.activity.database.model;

public class Reminder {

    public static final String TABLE_NAME = "reminder";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_REMINDER_TIME = "time";
    public static final String COLUMN_REMINDER_TITLE = "title";
    public static final String COLUMN_REMINDER_REPEAT = "repeat";

    private int id;
    private long time;
    private String title;
    private int repeat;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_REMINDER_TIME + " INTEGER,"
                    + COLUMN_REMINDER_TITLE + " TEXT,"
                    + COLUMN_REMINDER_REPEAT + " TEXT"
                    + ")";

    public Reminder() {
    }

    public Reminder(int id, long time, String title, int repeat) {
        this.id = id;
        this.time = time;

        this.title = title;
        this.repeat = repeat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public String getReminder() {
        return title;
    }

    public void setReminder(String title) {
        this.title = title;
    }
}
