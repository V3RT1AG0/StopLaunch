package com.gamerequirements.SaveCofig;

/**
 * Created by v3rt1ag0 on 12/3/17.
 */

public class Information
{
    String title;
    int id;
    Information(int id, String title)
    {
        this.id=id;
        this.title=title;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
