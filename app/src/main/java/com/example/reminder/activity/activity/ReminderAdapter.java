package com.example.reminder.activity.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.activity.database.model.Reminder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {

    private Context context;
    private List<Reminder> reminderList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView timeTV;
        public TextView title;
        public TextView intervalTV;
        public ImageView img;
        public ImageView repeatImg;

        public MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imageView);
            timeTV = view.findViewById(R.id.timeTV);
            title = view.findViewById(R.id.titleET);
            intervalTV = view.findViewById(R.id.intevalTV);
            repeatImg = view.findViewById(R.id.repeatImg);
        }
    }

    public ReminderAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);

        DateFormat timeReminder = new SimpleDateFormat("HH:mm");
        Date resultTime = new Date(reminder.getTime());
        String time = timeReminder.format(resultTime);
        holder.timeTV.setText(time);

        holder.title.setText(reminder.getTitle());

        DateFormat timeRepeat = new SimpleDateFormat("mm:ss");
        Date resultRepeat = new Date(reminder.getRepeatType());
        String timeRep = timeRepeat.format(resultRepeat);
        if (reminder.getRepeatType() == 3600000) {
            holder.intervalTV.setText(R.string.repeat_after_hour);
        } else if (reminder.getRepeatType() == 60000) {
            holder.intervalTV.setText(String.format(Locale.getDefault(), "%s %s %s", context.getString(R.string.repeat_after), timeRep, context.getString(R.string.minute)));
        } else if (reminder.getRepeatType() == 86400000) {
            holder.intervalTV.setText(R.string.repeat_once_day);
        } else {
            holder.intervalTV.setText(String.format(Locale.getDefault(), "%s %s %s", context.getString(R.string.repeat_after), timeRep, context.getString(R.string.minutes)));
        }

        if (reminder.getRepeat() == 1) {
            holder.repeatImg.setVisibility(View.VISIBLE);
            holder.intervalTV.setVisibility(View.VISIBLE);
        } else {
            holder.intervalTV.setVisibility(View.INVISIBLE);
            holder.repeatImg.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }
}
