package com.kevinpelgrims.pillreminder.views;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kevinpelgrims.pillreminder.R;

import java.util.Calendar;

public class AddReminderFragment extends PRFragment {
    private int mSelectedHour, mSelectedMinute;
    private TextView mAlarmTimeView;

    public static AddReminderFragment newInstance() {
        return new AddReminderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_reminder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAlarmTimeView = (TextView) view.findViewById(R.id.add_reminder_alarm_time);
        mAlarmTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        showTimePickerDialog();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mSelectedHour = hourOfDay;
                mSelectedMinute = minute;
                setAlarmTimeText();
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 0, true);
        timePickerDialog.setTitle("Select time");
        timePickerDialog.show();
    }

    private void setAlarmTimeText() {
        String formattedTime = String.format("%02d:%02d", mSelectedHour, mSelectedMinute);
        mAlarmTimeView.setText(formattedTime);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_reminder, menu);
    }
}
