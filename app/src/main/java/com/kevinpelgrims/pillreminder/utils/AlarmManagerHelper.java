package com.kevinpelgrims.pillreminder.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import com.kevinpelgrims.pillreminder.backend.reminderApi.model.Reminder;
import com.kevinpelgrims.pillreminder.receivers.ReminderAlarmReceiver;

import java.util.Calendar;
import java.util.List;

public class AlarmManagerHelper {
    public static void setUpReminderAlarms(Context context, List<Reminder> reminders) {
        cancelAlarms(context, reminders);
        setAlarms(context, reminders);
    }

    public static void setUpReminderAlarm(Context context, Reminder reminder) {
        cancelAlarm(context, reminder);
        setAlarm(context, reminder);
    }

    private static void cancelAlarm(Context context, Reminder reminder) {
        PendingIntent intent = ReminderAlarmReceiver.createPendingIntent(context, reminder, ReminderAlarmReceiver.BROADCAST_TYPE_ALARM_TRIGGER);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(intent);
    }

    private static void cancelAlarms(Context context, List<Reminder> reminders) {
        if (reminders == null || reminders.isEmpty()) return;

        for (Reminder reminder : reminders) {
            cancelAlarm(context, reminder);
        }
    }

    private static void setAlarm(Context context, Reminder reminder) {
        // All repeating alarms are inexact since API 19, so we can't use those.
        // Instead we'll have to set a new alarm every single day when the old one is discarded.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
        Calendar alarmTime = Calendar.getInstance();
        if (nowHour > reminder.getHour() || (nowHour == reminder.getHour() && nowMinute >= reminder.getMinute())){
            alarmTime.add(Calendar.DATE, 1);
        }
        alarmTime.set(Calendar.HOUR_OF_DAY, reminder.getHour());
        alarmTime.set(Calendar.MINUTE, reminder.getMinute());
        alarmTime.set(Calendar.SECOND, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), ReminderAlarmReceiver.createPendingIntent(context, reminder, ReminderAlarmReceiver.BROADCAST_TYPE_ALARM_TRIGGER));
        }
        else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), ReminderAlarmReceiver.createPendingIntent(context, reminder, ReminderAlarmReceiver.BROADCAST_TYPE_ALARM_TRIGGER));
        }
    }

    private static void setAlarms(Context context, List<Reminder> reminders) {
        if (reminders == null || reminders.isEmpty()) return;

        for (Reminder reminder : reminders) {
            setAlarm(context, reminder);
        }
    }
}
