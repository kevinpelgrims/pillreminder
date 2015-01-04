package com.kevinpelgrims.pillreminder.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.kevinpelgrims.pillreminder.R;

public class ReminderAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra("id", 0);
        String name = intent.getStringExtra("name");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Take your pill!")
                .setContentText(name)
                //.setSound(Uri.parse(""))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setVibrate(new long[]{0, 100, 100})
                //.setFullScreenIntent(fullScreenIntent, true)
                .addAction(R.drawable.ic_launcher, "Dismiss", null) //TODO: intent
                .addAction(R.drawable.ic_launcher, "Snooze", null); //TODO: intent

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int)id, builder.build());
    }
}
