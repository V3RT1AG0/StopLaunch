package com.gamerequirements.Blog;

/**
 * Created by v3rt1ag0 on 7/7/18.
 */

public class Information
{
    String title,subtitle,imgvideurl;
    int id,category;
    public Information(int id,String title,String subtitle,String imgvideurl,int category){
        this.title= title;
        this.id = id;
        this.category= category;
        this.subtitle= subtitle;
        this.imgvideurl = imgvideurl;
    }

    public int getCategory()
    {
        return category;
    }

    public int getId()
    {
        return id;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public String getImgvideurl()
    {
        return imgvideurl;
    }

    public String getTitle()
    {
        return title;
    }
}

