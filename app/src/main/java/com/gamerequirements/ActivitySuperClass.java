package com.gamerequirements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
        myToolbar.findViewById(R.id.Share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    int applicationNameId = getApplicationContext().getApplicationInfo().labelRes;
                    final String appPackageName = getApplicationContext().getPackageName();
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getApplicationContext().getString(applicationNameId));
                    String text = "Check out this application";
                    String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
                    i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
                    startActivity(Intent.createChooser(i, "Share link:"));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
