package com.kevinpelgrims.pillreminder.views;

import android.os.Bundle;
import android.view.MenuItem;

import com.kevinpelgrims.pillreminder.R;

public class MainActivity extends PRActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MainFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, AddReminderFragment.newInstance())
                        .addToBackStack("addReminder")
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
