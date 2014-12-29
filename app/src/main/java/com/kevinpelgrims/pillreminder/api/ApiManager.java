package com.kevinpelgrims.pillreminder.api;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.kevinpelgrims.pillreminder.Constants;
import com.kevinpelgrims.pillreminder.backend.reminderApi.ReminderApi;
import com.kevinpelgrims.pillreminder.backend.reminderApi.model.Reminder;

import java.io.IOException;

public class ApiManager {
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
                .setRootUrl(Constants.API_URL);
        if (Constants.ENVIRONMENT.equals(Constants.ENVIRONMENT_DEBUG)) {
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

    public void insertReminder(final Reminder reminder, final Callback<Reminder> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Reminder reminderResponse = mReminderApi.insertReminder(reminder).execute();
                    Log.d("Reminder API", "Inserted reminder with ID " + reminder.getId());
                    callback.success(reminderResponse);
                } catch (IOException e) {
                    Log.d("Reminder API", "Failed to insert reminder", e);
                    callback.error(new Error(e.getMessage(), e));
                }
            }
        }).start();
    }
}
