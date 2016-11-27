package com.gamerequirements;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by v3rt1ag0 on 11/25/16.
 */

public class ActivitySuperClass extends AppCompatActivity
{
    @Override
    protected void onStart()
    {
        super.onStart();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
