package com.kevinpelgrims.pillreminder.views;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kevinpelgrims.pillreminder.R;
import com.kevinpelgrims.pillreminder.api.ApiManager;
import com.kevinpelgrims.pillreminder.backend.reminderApi.model.Reminder;
import com.kevinpelgrims.pillreminder.utils.Formatter;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddReminderFragment extends PRFragment {
    private int mSelectedHour = 8, mSelectedMinute = 0;
    @InjectView(R.id.add_reminder_alarm_time) TextView mAlarmTimeView;
    @InjectView(R.id.add_reminder_pill_name) TextView mNameView;
    @InjectView(R.id.add_reminder_note) TextView mNoteView;

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
        View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showTimePickerDialog();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_reminder, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            saveReminder();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.add_reminder_alarm_time)
    public void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mSelectedHour = hourOfDay;
                mSelectedMinute = minute;
                setAlarmTimeText();
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 0, true);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setAlarmTimeText();
            }
        });
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setAlarmTimeText();
                    }
                });
        timePickerDialog.show();
    }

    private void setAlarmTimeText() {
        mAlarmTimeView.setText(Formatter.formatTime(mSelectedHour, mSelectedMinute));
    }

    private void saveReminder() {
        Reminder reminder = new Reminder();
        reminder.setHour(mSelectedHour);
        reminder.setMinute(mSelectedMinute);
        reminder.setPillName(mNameView.getText().toString());
        reminder.setNote(mNoteView.getText().toString());

        ApiManager.getInstance().insertReminder(reminder, new ApiManager.Callback<Reminder>() {
            @Override
            public void success(Reminder response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Reminder saved", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void error(Error error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Saving reminder failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
