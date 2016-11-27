package com.gamerequirements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gamerequirements.Requirements.GameListActivity;

public class Splash extends ActivitySuperClass
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                startActivity(new Intent(Splash.this,GameListActivity.class));
            }
        }).start();
        setContentView(R.layout.activity_splash);
    }
}
