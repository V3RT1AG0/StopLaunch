package com.gamerequirements;

/**
 * Created by v3rt1ag0 on 3/28/17.
 * Fire ase Class File
 */

public class StopLaunch
{
    String server_url;
    int db_version;
    int forcemaintenance;
    int newVersion;

    StopLaunch(String server_url, int db_version,int forcemaintenance,int newVersion)
    {
        this.server_url=server_url;
        this.db_version=db_version;
        this.forcemaintenance=forcemaintenance;
        this.newVersion=newVersion;
    }

    StopLaunch()
    {

    }

    public int getdb_version()
    {
        return db_version;
    }

    public String getserver_url()
    {
        return server_url;
    }

    public int getForcemaintenance()
    {
        return forcemaintenance;
    }

    public int getNewVersion()
    {
        return newVersion;
    }
}
