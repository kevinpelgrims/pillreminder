package com.kevinpelgrims.pillreminder.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kevinpelgrims.pillreminder.R;
import com.kevinpelgrims.pillreminder.ReminderRecyclerItemClickListener;
import com.kevinpelgrims.pillreminder.adapters.ReminderAdapter;
import com.kevinpelgrims.pillreminder.api.ApiManager;
import com.kevinpelgrims.pillreminder.backend.reminderApi.model.Reminder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainFragment extends PRFragment {
    private OnMainFragmentInteractionListener mListener;

    @InjectView(R.id.reminders_list) RecyclerView remindersListView;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMainFragmentInteractionListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMainFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Load empty set of data into recyclerview when it's inflated,
        // otherwise it starts measuring null objects and it crashes the whole thing
        setUpReminderListView(new ArrayList<Reminder>());
        setUpReminderList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    private void setUpReminderList() {
        ApiManager.getInstance().listReminder(new ApiManager.Callback<List<Reminder>>() {
            @Override
            public void success(final List<Reminder> reminders) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), String.format("Retrieved all reminders, all %d of them!", reminders.size()), Toast.LENGTH_SHORT).show();
                        setUpReminderListView(reminders);
                    }
                });
            }

            @Override
            public void error(Error error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Retrieving reminders failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setUpReminderListView(final List<Reminder> reminders) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        remindersListView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new ReminderAdapter(getActivity(), reminders);
        remindersListView.setAdapter(adapter);

        remindersListView.removeOnItemTouchListener(itemClickListener);
        remindersListView.addOnItemTouchListener(itemClickListener);
    }

    final ReminderRecyclerItemClickListener itemClickListener = new ReminderRecyclerItemClickListener(getActivity(), new ReminderRecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            mListener.OnReminderClick(((ReminderAdapter) remindersListView.getAdapter()).getItem(position));
        }
    });

    public interface OnMainFragmentInteractionListener {
        public void OnReminderClick(Reminder reminder);
    }
}
