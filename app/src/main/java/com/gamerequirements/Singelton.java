package com.gamerequirements;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;

/**
 * Created by v3rt1ag0 on 7/13/18.
 */

public class Singelton
{
    private static YouTubePlayer youTubePlayer;

    public static void setYouTubePlayer(YouTubePlayer youTubePlayer)
    {
        Singelton.youTubePlayer = youTubePlayer;
    }

    public static YouTubePlayer getYouTubePlayer()
    {
        return youTubePlayer;
    }
}
