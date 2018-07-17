package com.gamerequirements.Utils;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.gamerequirements.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

public class YoutubeFullScreenActivity extends AppCompatActivity
{

    YouTubePlayerView youTubePlayerView;
    String youtbeId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_full_screen);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youtbeId = getIntent().getStringExtra("id");
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