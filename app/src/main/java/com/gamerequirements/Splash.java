package com.gamerequirements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gamerequirements.Requirements.GameListActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Splash extends ActivitySuperClass
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPref = MyApplication.getContext().getSharedPreferences(
                "com.gamerequirements", Context.MODE_PRIVATE);
        final int stored_db_version= sharedPref.getInt("dbv", 0);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                BufferedReader in = null;
                try
                {
                    URL url;
                    String ServerURL;
                    int current_dp_version;
                    url = new URL("http://pastebin.com/raw/UPLBJJtz");
                    while (in == null)
                    {
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                    }
                    ServerURL = in.readLine();
                    Singelton.setURL(ServerURL);
                    current_dp_version= Integer.parseInt(in.readLine());
                    Singelton.setDatabaseversion(current_dp_version);
                    if(current_dp_version==stored_db_version)
                    {
                        Singelton.setDatabaseisuptodate(true);
                    }
                    else
                    {
                        Singelton.setDatabaseisuptodate(false);
                    }
                    in.close();
                    Log.d("Server"+ServerURL,Singelton.getURL());
                    startActivity(new Intent(Splash.this,GameListActivity.class));
                }
                catch (java.io.IOException e)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            Toast.makeText(Splash.this,"Please check your internet connection and try again",Toast.LENGTH_SHORT).show();
                        }
                    });
                    Splash.this.finish();
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
