package com.example.reminder.activity.activity;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.activity.database.model.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder>{

private Context context;
private List<Reminder> reminderList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView time;
    public ImageView img;
    public TextView title;

    public MyViewHolder(View view) {
        super(view);
        img = view.findViewById(R.id.imageView);
        time = view.findViewById(R.id.timeTV);
        title = view.findViewById(R.id.titleTV);
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

        holder.time.setText(reminder.getTime());
//        holder.img.setText(Html.fromHtml("&#8226;"));
        holder.title.setText(reminder.getTitle());
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

//    private String formatDate(String dateStr) {
//        try {
//            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = fmt.parse(dateStr);
//            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
//            return fmtOut.format(date);
//        } catch (ParseException e) {
//        }
//        return "";
//    }
}
