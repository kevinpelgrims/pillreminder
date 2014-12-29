package com.kevinpelgrims.pillreminder.views;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.kevinpelgrims.pillreminder.R;

public class MainActivity extends PRActivity implements FragmentManager.OnBackStackChangedListener {
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
                getSupportFragmentManager().popBackStackImmediate();
                return true;
            case R.id.action_add:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, AddReminderFragment.newInstance())
                        .addToBackStack("addReminder")
                        .commit();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return true;
            case R.id.action_save:
                //getSupportFragmentManager().popBackStackImmediate();
                return false;
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
}
