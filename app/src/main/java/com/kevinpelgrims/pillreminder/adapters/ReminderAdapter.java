package com.kevinpelgrims.pillreminder.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kevinpelgrims.pillreminder.R;
import com.kevinpelgrims.pillreminder.backend.reminderApi.model.Reminder;
import com.kevinpelgrims.pillreminder.utils.Formatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private Context context;
    private List<Reminder> reminders;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.widget_reminder_pill_name) public TextView pillName;
        @InjectView(R.id.widget_reminder_interval) public TextView interval;
        @InjectView(R.id.widget_reminder_note) public TextView note;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public ReminderAdapter(Context context, List<Reminder> reminders) {
        this.context = context;
        this.reminders = reminders != null ? reminders : new ArrayList<Reminder>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.widget_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        holder.pillName.setText(reminder.getPillName());
        String interval = context.getString(R.string.interval_at,
                context.getString(R.string.interval_daily),
                Formatter.formatTime(reminder.getHour(), reminder.getMinute()));
        holder.interval.setText(interval);
        holder.note.setText(reminder.getNote());
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public Reminder getItem(int position) {
        return reminders.get(position);
    }
}
