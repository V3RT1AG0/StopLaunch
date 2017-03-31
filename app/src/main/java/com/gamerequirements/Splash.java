package com.gamerequirements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gamerequirements.Requirements.GameListActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Splash extends AppCompatActivity
{
    DatabaseReference databaseref;
    int stored_db_version;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPref = MyApplication.getContext().getSharedPreferences(
                "com.gamerequirements", Context.MODE_PRIVATE);
        databaseref = FirebaseDatabase.getInstance().getReference();
        stored_db_version = sharedPref.getInt("dbv", 0);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if (!hasInternetAccess(Splash.this))
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

    void getdatafrompastebin()
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
            current_dp_version = Integer.parseInt(in.readLine());
            Singelton.setDatabaseversion(current_dp_version);
            if (current_dp_version == stored_db_version)
            {
                Singelton.setDatabaseisuptodate(true);
            } else
            {
                Singelton.setDatabaseisuptodate(false);
            }
            in.close();
            Log.d("Server" + ServerURL, Singelton.getURL());
            startActivity(new Intent(Splash.this, GameListActivity.class));
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
                int current_dp_version = def.getdb_version();
                Singelton.setURL(ServerURL);
                Singelton.setDatabaseversion(current_dp_version);
                if (current_dp_version == stored_db_version)
                {
                    Singelton.setDatabaseisuptodate(true);
                } else
                {
                    Singelton.setDatabaseisuptodate(false);
                }
                startActivity(new Intent(Splash.this, GameListActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }


    public boolean hasInternetAccess(Context context)
    {


        if (isNetworkAvailable(context))
        {
            try
            {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e)
            {
                Log.e("tag", "Error checking internet connection", e);
            }
        } else
        {
            Log.d("TAG", "No network available!");
        }
        return false;
    }

    private boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
