package com.kevinpelgrims.pillreminder.api;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.kevinpelgrims.pillreminder.BuildValues;
import com.kevinpelgrims.pillreminder.Constants;
import com.kevinpelgrims.pillreminder.backend.reminderApi.ReminderApi;
import com.kevinpelgrims.pillreminder.backend.reminderApi.model.Reminder;

import java.io.IOException;
import java.util.List;

public class ApiManager {
    private static final String TAG = "Reminder API";

    private static ApiManager mInstance;
    private static ReminderApi mReminderApi;

    private String mApiUrl;

    public static ApiManager getInstance() {
        if (mInstance == null) {
            mInstance = new ApiManager();
        }
        if (mReminderApi == null) {
            initializeReminderApi();
        }
        return mInstance;
    }

    private static void initializeReminderApi() {
        ReminderApi.Builder builder = new ReminderApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                .setRootUrl(BuildValues.API_URL);
        if (BuildValues.ENVIRONMENT.equals(Constants.ENVIRONMENT_DEBUG)) {
            builder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                    abstractGoogleClientRequest.setDisableGZipContent(true);
                }
            });
        }
        mReminderApi = builder.build();
    }

    public void setApiUrl(String mApiUrl) {
        this.mApiUrl = mApiUrl;
    }

    public interface Callback<T> {
        void success(T response);
        void error(Error error);
    }

    public void listReminder(final Callback<List<Reminder>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Reminder> reminders = mReminderApi.listReminder().execute().getItems();
                    Log.d(TAG, "Successfully retrieved list of reminders");
                    callback.success(reminders);
                } catch (IOException e) {
                    Log.d(TAG, "Failed to get list of reminders", e);
                    callback.error(new Error(e.getMessage(), e));
                }
            }
        }).start();
    }

    public void getReminder(final Long id, final Callback<Reminder> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Reminder reminderResponse = mReminderApi.getReminder(id).execute();
                    Log.d(TAG, "Got reminder with ID " + id);
                    callback.success(reminderResponse);
                } catch (IOException e) {
                    Log.d(TAG, "Failed to get reminder with ID " + id);
                    callback.error(new Error(e.getMessage(), e));
                }
            }
        }).start();
    }

    public void insertReminder(final Reminder reminder, final Callback<Reminder> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Reminder reminderResponse = mReminderApi.insertReminder(reminder).execute();
                    Log.d(TAG, "Inserted reminder with ID " + reminder.getId());
                    callback.success(reminderResponse);
                } catch (IOException e) {
                    Log.d(TAG, "Failed to insert reminder", e);
                    callback.error(new Error(e.getMessage(), e));
                }
            }
        }).start();
    }

    public void updateReminder(final Reminder reminder, final Callback<Reminder> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Reminder reminderResponse = mReminderApi.updateReminder(reminder).execute();
                    Log.d(TAG, "Updated reminder with ID " + reminder.getId());
                    callback.success(reminderResponse);
                } catch (IOException e) {
                    Log.d(TAG, "Failed to update reminder", e);
                    callback.error(new Error(e.getMessage(), e));
                }
            }
        }).start();
    }

    public void deleteReminder(final Long id, final Callback<Boolean> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mReminderApi.deleteReminder(id).execute();
                    Log.d(TAG, "Deleted reminder with ID " + id);
                    callback.success(true);
                } catch (IOException e) {
                    Log.d(TAG, "Failed to delete reminder with ID " + id);
                    callback.error(new Error(e.getMessage(), e));
                }
            }
        }).start();
    }
}
