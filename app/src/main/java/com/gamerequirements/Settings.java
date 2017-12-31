package com.gamerequirements;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.gamerequirements.SaveCofig.MainActivityConfig;
import com.google.firebase.messaging.FirebaseMessaging;

public class Settings extends AppCompatActivity implements Switch.OnCheckedChangeListener
{
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch myswitch = (Switch) findViewById(R.id.notificationSwitch);
        pref = Settings.this.getSharedPreferences(Singelton.getSharedPrefrenceKey(),MODE_PRIVATE);
        editor = pref.edit();
        myswitch.setOnCheckedChangeListener(this);
        myswitch.setChecked(pref.getBoolean("notification_toggle",true));

    }

    public void openConfigs(View view)
    {
        startActivity(new Intent(this, MainActivityConfig.class));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if(isChecked)
        {
            FirebaseMessaging.getInstance().subscribeToTopic("notifications");
            editor.putBoolean("notification_toggle",true);
            editor.commit();
        }
        else
        {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("notifications");
            editor.putBoolean("notification_toggle",false);
            editor.commit();
        }
    }
}
