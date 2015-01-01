package com.kevinpelgrims.pillreminder.views;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.kevinpelgrims.pillreminder.R;
import com.kevinpelgrims.pillreminder.backend.reminderApi.model.Reminder;
import com.kevinpelgrims.pillreminder.utils.KeyboardHelper;

public class MainActivity extends PRActivity implements FragmentManager.OnBackStackChangedListener, PRFragment.OnPRFragmentInteractionListener, MainFragment.OnMainFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MainFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                KeyboardHelper.hideSoftKeyboard(MainActivity.this);
                getSupportFragmentManager().popBackStackImmediate();
                return true;
            case R.id.action_add:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, AddReminderFragment.newInstance())
                        .addToBackStack("addReminder")
                        .commit();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof MainFragment) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void OnFragmentCloseRequest() {
        KeyboardHelper.hideSoftKeyboard(MainActivity.this);
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void OnReminderClick(Reminder reminder) {
        EditReminderFragment fragment = EditReminderFragment.newInstance(reminder);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("edit")
                .commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
