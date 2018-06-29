package com.gamerequirements;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.gamerequirements.Notification.NotificationActivity;
import com.gamerequirements.Requirements.TabbedActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Splash extends AppCompatActivity
{
    DatabaseReference databaseref;
    SharedPreferences.Editor editor;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPref = MyApplication.getContext().getSharedPreferences(
                "com.gamerequirements", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        databaseref = FirebaseDatabase.getInstance().getReference();
        Log.d("Firebase", "token " + FirebaseInstanceId.getInstance().getToken());

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if (!isNetworkAvailable(Splash.this))
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(Splash.this, "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Splash.this.finish();
                } else
                {
                    getdatafrompastebin();
                }
            }
        }).start();


    }

    /*private Intent getRequirementContentIntent(Bundle bundle)
    {
        return new Intent(this, Requirement_content.class).putExtras(bundle);
    }*/

    void getdatafrompastebin()
    {
        BufferedReader in = null;
        try
        {
            URL url;
            String ServerURL;
            //int current_dp_version;
            url = new URL("http://pastebin.com/raw/CEnw1cxr");
            while (in == null)
            {
                in = new BufferedReader(new InputStreamReader(url.openStream()));
            }
            ServerURL = in.readLine();
            MyApplication.setURL(ServerURL);
            editor.putString("url", ServerURL);
            editor.commit();

            int Forcemaintenance = Integer.parseInt(in.readLine());
            if (Forcemaintenance == 1)
            {
                in.close();
                return;
            }
            in.close();
            Log.d("Server" + ServerURL, MyApplication.getURL());
            startNextActivity();

        } catch (Exception e)
        {
            e.printStackTrace();
            tryfromfirebasedatabase();
        }
    }

    private void startNextActivity()
    {

        if (getIntent().hasExtra("update"))
        {
            intent = new Intent(Splash.this, NotificationActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            stackBuilder.startActivities();
        } else
        {
            intent = new Intent(Splash.this, TabbedActivity.class);
            startActivity(intent);
        }

    }

    void tryfromfirebasedatabase()
    {
        databaseref.child("StopLaunchV3").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.d("log", dataSnapshot.getKey());
                StopLaunch def = dataSnapshot.getValue(StopLaunch.class);
                String ServerURL = def.getserver_url();
                int Forcemaintenance = def.getForcemaintenance();
                if (Forcemaintenance == 1)
                    return;
                MyApplication.setURL(ServerURL);
                editor.putString("url", ServerURL);
                editor.commit();
                startNextActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(Splash.this, "Bad network connection. Please try again later", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
