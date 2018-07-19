package com.gamerequirements.Utils;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.gamerequirements.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

public class YoutubeFullScreenActivity extends AppCompatActivity
{

    YouTubePlayerView youTubePlayerView;
    String youtbeId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube_full_screen);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youtbeId = getIntent().getStringExtra("id");

        youTubePlayerView.toggleFullScreen();
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener()
        {
            @Override
            public void onYouTubePlayerEnterFullScreen()
            {

            }

            @Override
            public void onYouTubePlayerExitFullScreen()
            {
                YoutubeFullScreenActivity.this.finish();
            }
        });
        youTubePlayerView.initialize(new YouTubePlayerInitListener()
        {
            @Override
            public void onInitSuccess(@NonNull final YouTubePlayer youTubePlayer)
            {
                youTubePlayer.addListener(new AbstractYouTubePlayerListener()
                {
                    @Override
                    public void onReady()
                    {
                        loadVideo(youTubePlayer, youtbeId);
                    }
                });
            }
        },true);

    }

    private void loadVideo(YouTubePlayer youTubePlayer, String videoId)
    {
        if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED)
            youTubePlayer.loadVideo(videoId, 0);
        else
            youTubePlayer.cueVideo(videoId, 0);
    }
}