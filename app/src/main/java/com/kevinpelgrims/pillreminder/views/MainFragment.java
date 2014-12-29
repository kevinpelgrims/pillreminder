package com.kevinpelgrims.pillreminder.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kevinpelgrims.pillreminder.R;
import com.kevinpelgrims.pillreminder.api.ApiManager;
import com.kevinpelgrims.pillreminder.backend.reminderApi.model.Reminder;

import java.util.List;

public class MainFragment extends PRFragment {
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpReminderList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    private void setUpReminderList() {
        ApiManager.getInstance().listReminder(new ApiManager.Callback<List<Reminder>>() {
            @Override
            public void success(final List<Reminder> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), String.format("Retrieved all reminders, all %d of them!", response.size()), Toast.LENGTH_LONG).show();
                        //TODO
                    }
                });
            }

            @Override
            public void error(Error error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Retrieving reminders failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
