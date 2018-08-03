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
import com.gamerequirements.SaveCofig.SaveFirstConfig;
import com.google.firebase.analytics.FirebaseAnalytics;
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
    SharedPreferences sharedPref;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPref = MyApplication.getContext().getSharedPreferences(
                "com.gamerequirements", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        databaseref = FirebaseDatabase.getInstance().getReference();
        Log.d("Firebase", "token " + FirebaseInstanceId.getInstance().getToken());

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
               if (!isNetworkAvailable())
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(Splash.this);
                            Bundle bundle = new Bundle();
                            bundle.putString("response","No internet access detected");
                            firebaseAnalytics.logEvent("NoInternetConnectionSplash",bundle);
                            Toast.makeText(Splash.this, "Unable to contact server. Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Splash.this.finish();
                } else
                getdatafrompastebin();

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
            final String ServerURL;
            String blogURL;
            //int current_dp_version;
            url = new URL("http://pastebin.com/raw/CEnw1cxr");
            while (in == null)
            {
                in = new BufferedReader(new InputStreamReader(url.openStream()));
            }
            ServerURL = in.readLine();
            blogURL = in.readLine();
            MyApplication.setBlogUrl(blogURL);
            MyApplication.setURL(ServerURL);
            editor.putString("url", ServerURL);
            editor.putString("blogurl", blogURL);
            editor.commit();

            int Forcemaintenance = Integer.parseInt(in.readLine());
            if (Forcemaintenance == 1)
            {
                in.close();
                return;
            }


            if (Integer.parseInt(in.readLine()) != BuildConfig.VERSION_CODE)
            {
                // return "forceupdate";
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                      //  Toast.makeText(getBaseContext(), "Debug:" + ServerURL, Toast.LENGTH_LONG).show();
                        Toast.makeText(getBaseContext(), "A newer version is available in Google Play", Toast.LENGTH_LONG).show();
                    }
                });
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

        if (appAlreadyOpened())
            if (getIntent().hasExtra("update"))
            {
                intent = new Intent(Splash.this, NotificationActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(intent);
                stackBuilder.startActivities();
            } else if (getIntent().hasExtra("postID"))
            {
                intent = new Intent(Splash.this, TabbedActivity.class).putExtra("slide", 1);
                startActivity(intent);
            } else
            {
                intent = new Intent(Splash.this, TabbedActivity.class);
                startActivity(intent);
            }
    }

    private boolean appAlreadyOpened()
    {
        if (!sharedPref.getBoolean("alreadyLaunched", false))  // if app is not already opened start save first config
        {
            editor.putBoolean("alreadyLaunched", true);
            editor.commit();
            startActivity(new Intent(this, SaveFirstConfig.class));
            return false;
        }
        return true;
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
                if (def.getForcemaintenance() == 1)
                    return;
                if (def.getNewVersion() == BuildConfig.VERSION_CODE)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getBaseContext(), "A newer version is available in Google Play", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                String ServerURL = def.getserver_url();
                String blogURL = def.getBlog_url();
                MyApplication.setURL(ServerURL);
                editor.putString("url", ServerURL);
                editor.putString("blogurl", blogURL);
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

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
