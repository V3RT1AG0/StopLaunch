package com.gamerequirements.Requirements;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by v3rt1ag0 on 10/11/16.
 */

public class Information
{
    String title,date,summary,genre;
    int id;
    Information(int id, String title, String summary, String genre,String date)
    {
        this.id=id;
        this.title=title;
        this.date=date;
        this.summary=summary;
        this.genre=genre;
    }

 /*   Information(Parcel parcel)
    {
        this.title=parcel.readString();
        this.id=parcel.readInt();
    }

    @Override
    public String getBody()
    {
        return this.title;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(title);
        parcel.writeInt(id);

    }

    public static final Creator<Information> CREATOR = new Creator<Information>() {


        @Override
        public Information createFromParcel(Parcel parcel)
        {
            return new Information(parcel);
        }

        @Override
        public Information[] newArray(int size) {
            return new Information[size];
        }
    };*/
}
