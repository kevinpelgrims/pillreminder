package com.kevinpelgrims.pillreminder.views;

import android.os.Bundle;

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
}
