package com.gamerequirements;

/**
 * Created by v3rt1ag0 on 10/11/16.
 */

public class Singelton
{
    private static String URL;
    private static final String imageurl="http://www.game-debate.com/pic.php?g_id=";



    public static String getURL()
    {
        return URL;
    }

    public static String getImageurl()
    {
        return imageurl;
    }

    public static void setURL(String URL)
    {
        Singelton.URL = URL;
    }
}
