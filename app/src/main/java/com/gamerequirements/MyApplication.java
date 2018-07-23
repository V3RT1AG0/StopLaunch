package com.gamerequirements;

import android.app.Application;
import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by v3rt1ag0 on 10/11/16.
 */

public class MyApplication extends Application
{
    public static MyApplication sinstance;
    private static String URL;
    private static final String imageurl = "http://www.game-debate.com/pic.php?g_id=";
    private static final String SharedPrefrenceKey = "gameRequirementsShared";
    private static String blogUrl;


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
        JodaTimeAndroid.init(this);
    }

    public static String getURL()
    {
        if (URL == null)
            setURL(MyApplication.getContext().getSharedPreferences(
                    "com.gamerequirements", Context.MODE_PRIVATE).getString("url", null));
        return URL;
    }

    public static String getSharedPrefrenceKey()
    {
        return SharedPrefrenceKey;
    }

    public static String getImageurl()
    {
        return imageurl;
    }

    public static void setURL(String URL)
    {
        MyApplication.URL = URL;
    }

    public static void setBlogUrl(String blogUrl)
    {
        MyApplication.blogUrl = blogUrl;
    }

    public static String getBlogUrl()
    {
        if (blogUrl == null)
            setBlogUrl(MyApplication.getContext().getSharedPreferences(
                    "com.gamerequirements", Context.MODE_PRIVATE).getString("blogurl", null));
        return blogUrl;
    }
}
