package com.example.reminder.activity.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.reminder.activity.database.model.Reminder;

import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reminder_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Reminder.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Reminder.TABLE_NAME);
        onCreate(db);
    }

    public long insertReminder(long time, String title, int repeat, long repeatType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Reminder.COLUMN_REMINDER_TIME, time);
        values.put(Reminder.COLUMN_REMINDER_TITLE, title);
        values.put(Reminder.COLUMN_REMINDER_REPEAT, repeat);
        values.put(Reminder.COLUMN_REMINDER_REPEAT_TYPE, repeatType);
        long id = db.insert(Reminder.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public Reminder getReminder(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Reminder.TABLE_NAME,
                new String[]{Reminder.COLUMN_ID, Reminder.COLUMN_REMINDER_TIME, Reminder.COLUMN_REMINDER_TITLE, Reminder.COLUMN_REMINDER_REPEAT, Reminder.COLUMN_REMINDER_REPEAT_TYPE},
                Reminder.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Reminder reminder = new Reminder(
                cursor.getInt(cursor.getColumnIndex(Reminder.COLUMN_ID)),
                cursor.getLong(cursor.getColumnIndex(Reminder.COLUMN_REMINDER_TIME)),
                cursor.getString(cursor.getColumnIndex(Reminder.COLUMN_REMINDER_TITLE)),
                cursor.getInt(cursor.getColumnIndex(Reminder.COLUMN_REMINDER_REPEAT)),
                cursor.getLong(cursor.getColumnIndex(Reminder.COLUMN_REMINDER_REPEAT_TYPE)));

        return reminder;
    }

    public List<Reminder> getAllReminder() {
        List<Reminder> reminders = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Reminder.TABLE_NAME + " ORDER BY " +
                Reminder.COLUMN_REMINDER_TIME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setId(cursor.getInt(cursor.getColumnIndex(Reminder.COLUMN_ID)));
                reminder.setTime(cursor.getLong(cursor.getColumnIndex(Reminder.COLUMN_REMINDER_TIME)));
                reminder.setTitle(cursor.getString(cursor.getColumnIndex(Reminder.COLUMN_REMINDER_TITLE)));
                reminder.setRepeat(cursor.getInt(cursor.getColumnIndex(Reminder.COLUMN_REMINDER_REPEAT)));
                reminder.setRepeatType(cursor.getLong(cursor.getColumnIndex(Reminder.COLUMN_REMINDER_REPEAT_TYPE)));
                reminders.add(reminder);
            } while (cursor.moveToNext());
        }
        db.close();
        return reminders;
    }

    public int getReminderCount() {
        String countQuery = "SELECT  * FROM " + Reminder.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Reminder.TABLE_NAME, Reminder.COLUMN_ID + " = ?",
                new String[]{String.valueOf(reminder.getId())});
        db.close();
    }

    public int updateReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Reminder.COLUMN_REMINDER_TIME, reminder.getTime());
        values.put(Reminder.COLUMN_REMINDER_TITLE, reminder.getTitle());
        values.put(Reminder.COLUMN_REMINDER_REPEAT, reminder.getRepeat());
        values.put(Reminder.COLUMN_REMINDER_REPEAT_TYPE, reminder.getRepeatType());

        return db.update(Reminder.TABLE_NAME, values, Reminder.COLUMN_ID + " = ?",
                new String[]{String.valueOf(reminder.getId())});
    }

    public void deleteAllReminder() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Reminder.TABLE_NAME);
        db.close();
    }
}
