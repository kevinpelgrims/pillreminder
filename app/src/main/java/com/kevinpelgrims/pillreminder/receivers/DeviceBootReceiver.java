package com.kevinpelgrims.pillreminder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kevinpelgrims.pillreminder.api.ApiManager;
import com.kevinpelgrims.pillreminder.backend.reminderApi.model.Reminder;
import com.kevinpelgrims.pillreminder.utils.AlarmManagerHelper;

import java.util.List;

public class DeviceBootReceiver extends BroadcastReceiver {
    private static final String TAG = "DeviceBootReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        ApiManager.getInstance().listReminder(new ApiManager.Callback<List<Reminder>>() {
            @Override
            public void success(List<Reminder> response) {
                if (response != null && response.size() > 0) {
                    AlarmManagerHelper.setUpReminderAlarms(context, response);
                    Log.d(TAG, String.format("Set %s reminders after device boot", response.size()));
                }
                else {
                    Log.d(TAG, "Didn't set any reminders after device boot.");
                }
            }

            @Override
            public void error(Error error) {
                //TODO: Maybe try again later?
                Log.e(TAG, "Failed to fetch reminders after device boot. No reminders are set.", error);
            }
        });
    }
}
