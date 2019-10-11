package com.example.reminder.activity.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.reminder.R;
import com.example.reminder.activity.database.DatabaseHelper;
import com.example.reminder.activity.database.model.Reminder;
import com.example.reminder.activity.utils.ItemDecoration;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String NOTIFICATION_ID = "";
    private ReminderAdapter adapter;
    private List<Reminder> reminderList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private ConstraintLayout cl;
    private TextView textView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.emptyTV);
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
                //showActionsDialog(position);
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
        View view = layoutInflaterAndroid.inflate(R.layout.add_reminder, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText timeET = view.findViewById(R.id.timeET);
        final EditText titleET = view.findViewById(R.id.titleET);
        TextView dialogTitle = view.findViewById(R.id.dialogTitleTV);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_reminder_title) : getString(R.string.lbl_edit_reminder_title));

        if (shouldUpdate && reminder != null) {
            timeET.setText(reminder.getTime());
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
                if (TextUtils.isEmpty(timeET.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter reminder!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }
                if (shouldUpdate && reminder != null) {
                    updateReminder(timeET.getText().toString(), titleET.getText().toString(), position);
                } else {
                    createReminder(timeET.getText().toString(), titleET.getText().toString());
                }
            }
        });
    }

    // Create Reminder
    private void createReminder(String time, String title) {
        long id = db.insertReminder(time, title);
        Reminder reminder = db.getReminder(id);
        if (reminder != null) {
            reminderList.add(0, reminder);
            adapter.notifyDataSetChanged();
            emptyReminder();
            notificationShow(title);
        }
    }

    // Update Reminder
    private void updateReminder(String time, String title, int position) {
        Reminder reminder = reminderList.get(position);
        reminder.setTime(time);
        reminder.setTitle(title);
        db.updateReminder(reminder);
        reminderList.set(position, reminder);
        adapter.notifyItemChanged(position);

        emptyReminder();
    }

    // Delete Reminder
    private void deleteReminder(int position) {
        db.deleteReminder(reminderList.get(position));
        reminderList.remove(position);
        adapter.notifyItemRemoved(position);
        emptyReminder();
    }

    private void emptyReminder() {
        if (db.getReminderCount() > 0) {
            cl.setVisibility(View.GONE);
        } else {
            cl.setVisibility(View.VISIBLE);
        }
    }

    private void notificationShow(String title) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_ID)
                        .setSmallIcon(R.drawable.ic_alert)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(title)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);
//                        .setContentIntent(MainActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = title;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            // 0 - ID of notification
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
