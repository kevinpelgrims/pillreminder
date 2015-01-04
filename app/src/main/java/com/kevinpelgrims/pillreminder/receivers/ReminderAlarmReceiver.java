package com.kevinpelgrims.pillreminder.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.kevinpelgrims.pillreminder.R;
import com.kevinpelgrims.pillreminder.backend.reminderApi.model.Reminder;
import com.kevinpelgrims.pillreminder.utils.AlarmManagerHelper;

import java.util.Calendar;

public class ReminderAlarmReceiver extends BroadcastReceiver {
    public static final int BROADCAST_TYPE_ALARM_TRIGGER = 0;
    public static final int BROADCAST_TYPE_ALARM_SNOOZE = 1;
    public static final int BROADCAST_TYPE_ALARM_DISMISS = 2;
    public static final int BROADCAST_TYPE_ALARM_DELETE = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra("type", 0);

        Reminder reminder = new Reminder();
        reminder.setId(intent.getLongExtra("id", 0));
        reminder.setPillName(intent.getStringExtra("name"));
        reminder.setHour(intent.getIntExtra("hour", 20));
        reminder.setMinute(intent.getIntExtra("minute", 0));
        reminder.setNote(intent.getStringExtra("note"));

        switch (type) {
            case BROADCAST_TYPE_ALARM_TRIGGER:
                triggerAlarm(context, reminder);
                break;
            case  BROADCAST_TYPE_ALARM_SNOOZE:
                dismissNotification(context, reminder);
                AlarmManagerHelper.setUpReminderAlarm(context, addFiveMinutes(reminder));
                break;
            case BROADCAST_TYPE_ALARM_DISMISS:
                dismissNotification(context, reminder);
                // Re-add alarm for the next day
                AlarmManagerHelper.setUpReminderAlarm(context, reminder);
                break;
            case BROADCAST_TYPE_ALARM_DELETE:
                // Re-add alarm for the next day
                AlarmManagerHelper.setUpReminderAlarm(context, reminder);
                break;
            default:
                break;
        }
    }

    private void triggerAlarm(Context context, Reminder reminder) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_description, reminder.getPillName()))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setVibrate(new long[]{0, 100, 100})
                //.setFullScreenIntent(fullScreenIntent, true)
                .addAction(R.drawable.ic_action_snooze, context.getString(R.string.action_snooze), createPendingIntent(context, reminder, BROADCAST_TYPE_ALARM_SNOOZE))
                .addAction(R.drawable.ic_action_done, context.getString(R.string.action_dismiss), createPendingIntent(context, reminder, BROADCAST_TYPE_ALARM_DISMISS))
                .setDeleteIntent(createPendingIntent(context, reminder, BROADCAST_TYPE_ALARM_DELETE));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(reminder.getId().intValue(), builder.build());
    }

    public static PendingIntent createPendingIntent(Context context, Reminder reminder, int type) {
        Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        intent.putExtra("type", type);
        intent.putExtra("id", reminder.getId());
        intent.putExtra("name", reminder.getPillName());
        intent.putExtra("hour", reminder.getHour());
        intent.putExtra("minute", reminder.getMinute());
        intent.putExtra("note", reminder.getNote());

        return PendingIntent.getBroadcast(context, type, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void dismissNotification(Context context, Reminder reminder) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(reminder.getId().intValue());
    }

    private Reminder addFiveMinutes(Reminder reminder) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, reminder.getHour());
        time.set(Calendar.MINUTE, reminder.getMinute());

        time.add(Calendar.MINUTE, 1);

        reminder.setHour(time.get(Calendar.HOUR_OF_DAY));
        reminder.setMinute(time.get(Calendar.MINUTE));

        return reminder;
    }
}
