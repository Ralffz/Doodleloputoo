package com.example.ralff.doodleloputoo.doodlejump.view;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import com.example.ralff.doodleloputoo.R;

public class AboutMeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
