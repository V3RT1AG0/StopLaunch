package com.gamerequirements;

/**
 * Created by v3rt1ag0 on 10/11/16.
 */

public class Singelton
{
    private static String URL;
    private static final String imageurl="http://www.game-debate.com/pic.php?g_id=";
    private static boolean databaseisuptodate;
    private  static int databaseversion;


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

    public static boolean isDatabaseisuptodate()
    {
        return databaseisuptodate;
    }

    public static void setDatabaseisuptodate(boolean databaseisuptodate)
    {
        Singelton.databaseisuptodate = databaseisuptodate;
    }

    public static void setDatabaseversion(int databaseversion)
    {
        Singelton.databaseversion = databaseversion;
    }

    public static int getDatabaseversion()
    {
        return databaseversion;
    }
}
