package com.example.reminder.activity.activity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.reminder.R;
import com.example.reminder.activity.database.DatabaseHelper;
import com.example.reminder.activity.database.model.Reminder;
import com.example.reminder.activity.utils.ItemDecoration;
import com.example.reminder.activity.utils.Receiver;
import com.example.reminder.activity.utils.RecyclerListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int NOTIFICATION_REMINDER = 100;

    private ReminderAdapter adapter;
    private List<Reminder> reminderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ConstraintLayout cl;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        cl = findViewById(R.id.CL);

        db = new DatabaseHelper(this);

        reminderList.addAll(db.getAllReminder());

        adapter = new ReminderAdapter(this, reminderList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(adapter);

        emptyReminder();

        // Touch Listener
        recyclerView.addOnItemTouchListener(new RecyclerListener(this,
                recyclerView, new RecyclerListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        // FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReminderDialog(false, null, -1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_delete_all) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(R.string.message_delete_all_reminder);
            builder.setPositiveButton(R.string.ok_btn,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            db.deleteAllReminder();
                            recreate();
                        }
                    })
                    .setNegativeButton(R.string.cancel_btn,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    ).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // Action Dialog
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showReminderDialog(true, reminderList.get(position), position);
                } else {
                    deleteReminder(position);
                }
            }
        });
        builder.show();
    }

    // Reminder Dialog
    private void showReminderDialog(final boolean shouldUpdate, final Reminder reminder, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.add_reminder_item, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final TimePicker timePicker = view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        final Switch switchBtn = view.findViewById(R.id.switchBtn);
        final EditText titleET = view.findViewById(R.id.titleET);
        TextView dialogTitle = view.findViewById(R.id.dialogTitleTV);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_reminder_title) : getString(R.string.lbl_edit_reminder_title));

        if (shouldUpdate && reminder != null) {
            titleET.setText(reminder.getTitle());
        }
        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                        if (isChecked) {
                            RAPP.repeatStatus = 1;
                        } else {
                            RAPP.repeatStatus = 0;
                        }
                    }
                });

                if (TextUtils.isEmpty(titleET.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter title!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }
                if (shouldUpdate && reminder != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        RAPP.hourNotification = timePicker.getHour();
                        RAPP.minuteNotification = timePicker.getMinute();
                        RAPP.titleNotification = titleET.getText().toString();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                        calendar.set(Calendar.MINUTE, timePicker.getMinute());
                        long time = calendar.getTimeInMillis();
                        updateReminder(time, titleET.getText().toString(), RAPP.repeatStatus, position);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        RAPP.hourNotification = timePicker.getHour();
                        RAPP.minuteNotification = timePicker.getMinute();
                        RAPP.titleNotification = titleET.getText().toString();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                        calendar.set(Calendar.MINUTE, timePicker.getMinute());
                        long time = calendar.getTimeInMillis();
                        createReminder(time, titleET.getText().toString(), RAPP.repeatStatus);
                    }
                }
            }
        });
    }

    // Create Reminder
    private void createReminder(long time, String title, int repeat) {
        long id = db.insertReminder(time, title, repeat);
        Reminder reminder = db.getReminder(id);
        if (reminder != null) {
            reminderList.add(0, reminder);
            adapter.notifyDataSetChanged();
            emptyReminder();
            createAlarm(RAPP.hourNotification, RAPP.minuteNotification);
        }
    }

    // Update Reminder
    private void updateReminder(long time, String title, int repeat, int position) {
        Reminder reminder = reminderList.get(position);
        reminder.setTime(time);
        reminder.setTitle(title);
        reminder.setRepeat(repeat);
        db.updateReminder(reminder);
        reminderList.set(position, reminder);
        adapter.notifyItemChanged(position);
        emptyReminder();
        createAlarm(RAPP.hourNotification, RAPP.minuteNotification);
    }

    // Delete Reminder
    private void deleteReminder(int position) {
        db.deleteReminder(reminderList.get(position));
        reminderList.remove(position);
        adapter.notifyItemRemoved(position);
        emptyReminder();
    }

    // Show Empty Reminder List
    private void emptyReminder() {
        if (db.getReminderCount() > 0) {
            cl.setVisibility(View.GONE);
        } else {
            cl.setVisibility(View.VISIBLE);
        }
    }

    // Alarm Manager
    private void createAlarm(int hour, int minute) {
        // AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, NOTIFICATION_REMINDER, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
