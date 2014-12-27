package com.kevinpelgrims.pillreminder.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kevinpelgrims.pillreminder.R;

public class AddReminderFragment extends PRFragment {
    public static AddReminderFragment newInstance() {
        return new AddReminderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_reminder, container, false);
    }
}
