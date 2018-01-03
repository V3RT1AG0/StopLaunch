package com.gamerequirements;

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
import com.gamerequirements.Requirements.GameListActivity;
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
    int stored_db_version;
Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPref = MyApplication.getContext().getSharedPreferences(
                "com.gamerequirements", Context.MODE_PRIVATE);
        databaseref = FirebaseDatabase.getInstance().getReference();
        stored_db_version = sharedPref.getInt("dbv", 0);
        Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());

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
                    if (getIntent().hasExtra("update")){
                        intent = new Intent(Splash.this, NotificationActivity.class);
                    }
                    else
                    {
                        intent = new Intent(Splash.this, GameListActivity.class);
                    }
                    startActivity(intent);

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
            Singelton.setURL(ServerURL);
            in.close();
            Log.d("Server" + ServerURL, Singelton.getURL());

        } catch (Exception e)
        {
            e.printStackTrace();
            tryfromfirebasedatabase();
        }
    }

    void tryfromfirebasedatabase()
    {
        databaseref.child("Default").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.d("log", dataSnapshot.getKey());
                Default def = dataSnapshot.getValue(Default.class);
                String ServerURL = def.getserver_url();
                Singelton.setURL(ServerURL);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

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
