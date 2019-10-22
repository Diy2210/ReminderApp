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

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder>{

private Context context;
private List<Reminder> reminderList;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView timeTV;
    public ImageView img;
    public TextView title;
    public ImageView repeatImg;

    public MyViewHolder(View view) {
        super(view);
        img = view.findViewById(R.id.imageView);
        timeTV = view.findViewById(R.id.timeTV);
        title = view.findViewById(R.id.titleET);
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

        DateFormat simple = new SimpleDateFormat("HH:mm");
        Date result = new Date(reminder.getTime());
        String time = simple.format(result);

        holder.timeTV.setText(time);
        holder.title.setText(reminder.getTitle());
        if (reminder.getRepeat() == 1) {
            holder.repeatImg.setVisibility(View.VISIBLE);
        } else {
            holder.repeatImg.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }
}
