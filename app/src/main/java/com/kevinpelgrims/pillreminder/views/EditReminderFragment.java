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

public class EditReminderFragment extends PRFragment {
    private static final String ARG_REMINDER_ID = "reminderId";

    private Reminder reminder;
    private int mSelectedHour = 8, mSelectedMinute = 0;

    @InjectView(R.id.add_reminder_alarm_time) TextView mAlarmTimeView;
    @InjectView(R.id.add_reminder_pill_name) TextView mNameView;
    @InjectView(R.id.add_reminder_note) TextView mNoteView;

    public static EditReminderFragment newInstance(Reminder reminder) {
        EditReminderFragment fragment = new EditReminderFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_REMINDER_ID, reminder.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            setUpReminder(getArguments().getLong(ARG_REMINDER_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_reminder, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveReminder();
                return true;
            case R.id.action_delete:
                deleteReminder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void setReminder(Reminder reminder) {
        this.reminder = reminder;
        mSelectedHour = reminder.getHour();
        mSelectedMinute = reminder.getMinute();
        setAlarmTimeText();
        mNameView.setText(reminder.getPillName());
        mNoteView.setText(reminder.getNote());
    }

    private void setAlarmTimeText() {
        mAlarmTimeView.setText(Formatter.formatTime(mSelectedHour, mSelectedMinute));
    }

    private void setUpReminder(long reminderId) {
        ApiManager.getInstance().getReminder(reminderId, new ApiManager.Callback<Reminder>() {
            @Override
            public void success(final Reminder reminderResponse) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setReminder(reminderResponse);
                    }
                });
            }

            @Override
            public void error(Error error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Getting reminder failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void saveReminder() {
        reminder.setHour(mSelectedHour);
        reminder.setMinute(mSelectedMinute);
        reminder.setPillName(mNameView.getText().toString());
        reminder.setNote(mNoteView.getText().toString());

        ApiManager.getInstance().updateReminder(reminder, new ApiManager.Callback<Reminder>() {
            @Override
            public void success(Reminder response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Reminder updated", Toast.LENGTH_SHORT).show();
                        onPRFragmentInteractionListener.OnFragmentCloseRequest();
                    }
                });
            }

            @Override
            public void error(Error error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Updating reminder failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void deleteReminder() {
        ApiManager.getInstance().deleteReminder(reminder.getId(), new ApiManager.Callback<Boolean>() {
            @Override
            public void success(Boolean response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Reminder deleted", Toast.LENGTH_SHORT).show();
                        onPRFragmentInteractionListener.OnFragmentCloseRequest();
                    }
                });
            }

            @Override
            public void error(Error error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Deleting reminder failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
