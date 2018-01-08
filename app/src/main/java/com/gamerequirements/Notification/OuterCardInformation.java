package com.gamerequirements.Notification;

import java.util.List;

/**
 * Created by v3rt1ag0 on 12/31/17.
 */

public class OuterCardInformation
{

    String data, count;
    List<InnerCardInformation> innerCardList;

    OuterCardInformation(String data, List<InnerCardInformation> innerCardList, String count)
    {
        String countText;
        if(count.equals("1"))
            countText = " Game added";
        else
            countText = " Games added";
        this.count = count + countText;
        this.data = data;
        this.innerCardList = innerCardList;
    }
}
