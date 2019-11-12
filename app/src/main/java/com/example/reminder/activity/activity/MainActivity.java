package com.example.reminder.activity.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.reminder.R;
import com.example.reminder.activity.database.DatabaseHelper;
import com.example.reminder.activity.database.model.Reminder;
import com.example.reminder.activity.utils.ItemDecoration;
import com.example.reminder.activity.utils.Receiver;
import com.example.reminder.activity.utils.RecyclerListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private ReminderAdapter adapter;
    private Reminder reminder;
    private List<Reminder> reminderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ConstraintLayout cl;
    private DatabaseHelper db;
    private long time;
    private ArrayAdapter<?> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bottomAppBar);

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
        return super.onOptionsItemSelected(item);
    }

    // Action Dialog
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{getString(R.string.edit_reminder), getString(R.string.delete_reminder)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_option);
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showReminderDialog(true, reminderList.get(position), position);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(true);
                    builder.setMessage(R.string.message_delete_reminder);
                    builder.setPositiveButton(R.string.delete_btn,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteReminder(position);
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

        final EditText titleET = view.findViewById(R.id.titleET);
        final Switch switchBtn = view.findViewById(R.id.switchBtn);
        final Spinner spinner = view.findViewById(R.id.spinner);

        if (shouldUpdate && reminder != null) {
            titleET.setText(reminder.getTitle());
            if (reminder.getRepeat() == 1) {
                switchBtn.setChecked(true);
                spinner.setVisibility(View.VISIBLE);
            } else {
                spinner.setVisibility(View.GONE);
            }
        }

        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton(shouldUpdate ? getString(R.string.update_btn) : getString(R.string.save_btn), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                    }
                })
                .setNegativeButton(R.string.cancel_btn,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        // TimePicker set time for edit reminder
        if (reminder != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(reminder.getTime());
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        }

        // Repeat Switch Button
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    RAPP.repeatStatus = 1;
                    spinner.setVisibility(View.VISIBLE);
                } else {
                    RAPP.repeatStatus = 0;
                    spinner.setVisibility(View.GONE);
                }
            }
        });

        // Spinner Interval Repeat
        arrayAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.repeat_interval, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {
                final String[] size_values = getResources().getStringArray(R.array.repeat_interval_milliseconds);
                RAPP.intervalRepeatMilliseconds = Integer.parseInt(size_values[selectedItemPosition]);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set value for spinner
        if (reminder != null) {
            if (reminder.getRepeatType() == 60000) {
                spinner.setSelection(0);
            } else if (reminder.getRepeatType() == 300000) {
                spinner.setSelection(1);
            } else if (reminder.getRepeatType() == 600000) {
                spinner.setSelection(2);
            } else if (reminder.getRepeatType() == 900000) {
                spinner.setSelection(3);
            } else if (reminder.getRepeatType() == 1200000) {
                spinner.setSelection(4);
            } else if (reminder.getRepeatType() == 1800000) {
                spinner.setSelection(5);
            } else if (reminder.getRepeatType() == 3600000) {
                spinner.setSelection(6);
            } else if (reminder.getRepeatType() == 86400000) {
                spinner.setSelection(7);
            }
        }

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check Empty Reminder Title
                if (TextUtils.isEmpty(titleET.getText().toString())) {
                    Toast.makeText(MainActivity.this, getString(R.string.enter_title), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }
                // Update Reminder Method
                if (shouldUpdate && reminder != null) {
                    RAPP.hourNotification = timePicker.getHour();
                    RAPP.minuteNotification = timePicker.getMinute();
                    RAPP.titleNotification = titleET.getText().toString();

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());
                    long time = calendar.getTimeInMillis();
                    RAPP.millisNotification = time;
                    updateReminder(time, titleET.getText().toString(), RAPP.repeatStatus, RAPP.intervalRepeatMilliseconds, position);
                } else {
                    // Create Reminder Method
                    RAPP.hourNotification = timePicker.getHour();
                    RAPP.minuteNotification = timePicker.getMinute();
                    RAPP.titleNotification = titleET.getText().toString();

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());
                    time = calendar.getTimeInMillis();
                    RAPP.millisNotification = time;
                    createReminder(time, titleET.getText().toString(), RAPP.repeatStatus, RAPP.intervalRepeatMilliseconds);
                }
            }
        });
    }

    // Create Reminder
    private void createReminder(long time, String title, int repeat, long repeatType) {
        long id = db.insertReminder(time, title, repeat, repeatType);
        reminder = db.getReminder(id);
        if (reminder != null) {
            reminderList.add(0, reminder);
            adapter.notifyDataSetChanged();
            RAPP.reminder_id = Math.toIntExact(id);

            createAlarm(RAPP.hourNotification, RAPP.minuteNotification, RAPP.reminder_id);
            emptyReminder();
        }
    }

    // Update Reminder
    private void updateReminder(long time, String title, int repeat, long repeatType, int position) {
        reminder = reminderList.get(position);
        int id1 = reminder.getId();

        db.deleteReminder(reminderList.get(position));
        reminderList.remove(position);

        createReminder(time, title, repeat, repeatType);

        deleteAlarm(id1);
    }

    // Delete Reminder
    private void deleteReminder(int position) {
        reminder = reminderList.get(position);
        int id = reminder.getId();

        db.deleteReminder(reminderList.get(position));
        reminderList.remove(position);
        adapter.notifyItemRemoved(position);

        deleteAlarm(id);
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
    private void createAlarm(int hour, int minute, int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (RAPP.repeatStatus == 1) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), RAPP.intervalRepeatMilliseconds, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }

    // Delete Alarm Manager
    private void deleteAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
