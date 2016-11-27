package com.gamerequirements;

import android.app.Application;
import android.content.Context;

/**
 * Created by adity on 27/06/2016.
 */

public class MyApplication extends Application
{
    public static MyApplication sinstance;

    public static MyApplication getInstance()
    {
        return (sinstance);

    }

    public static Context getContext()
    {
        return (sinstance.getApplicationContext());

    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        sinstance = this;  //or this also works


    }

}
