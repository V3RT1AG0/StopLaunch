package com.gamerequirements.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.gamerequirements.MyApplication;

/**
 * Created by v3rt1ag0 on 7/31/18.
 */

public class NotificationBroadCastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NotificationCheck","cleared");
        SharedPreferences sharedPrefs = MyApplication.getContext().getSharedPreferences("com.gamerequirements", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(MyFireBaseMessagingService.SETKEY);
        editor.remove(MyFireBaseMessagingService.KEY);
        editor.commit();
    }
}
