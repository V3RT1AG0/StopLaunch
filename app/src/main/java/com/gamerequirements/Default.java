package com.gamerequirements;

/**
 * Created by v3rt1ag0 on 3/28/17.
 */

public class Default
{
    String server_url;
    int db_version;
    Default(String server_url,int db_version)
    {
        this.server_url=server_url;
        this.db_version=db_version;
    }

    Default()
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
}
