package com.gamerequirements.Notification;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gamerequirements.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFireBaseMessagingService extends FirebaseMessagingService
{

    private static final String TAG = "MyFirebaseMsgService";
    Map mymap;
    int value;
    String key;
    Intent intent;
    Random rand;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        //Displaying data in log
        //It is optional
        rand= new Random();
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        mymap=remoteMessage.getData();
        Log.d("mymap",mymap.toString());
        if(mymap.containsKey("update"))
        {
            value = Integer.parseInt(mymap.get("update").toString());
            key="update";
            intent = new Intent(this, NotificationActivity.class);
        }
        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getBody(),value,key);

    }


    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody,int value,String key)
    {
        //Intent intent = new Intent(this, FloatingPost.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(key,value);
        /**this intent is only used when application is running and the given extras are provided.
         * ELSE if the application is closed the Splash screen will receive intent directly from Firebase and not frome here
         * Application ON:Firebase-This Service with extras from firebase-ACtivity specified by pendingintent with extras specified in this service
         * Application Closed:Firebase-Splash Screen with extras from firebase-Activity specified in Splash
         */
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Game Requirements")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyID = rand.nextInt(100)+1;
        if (notificationManager != null)
        {
            notificationManager.notify(notifyID, notificationBuilder.build());
        }
    }
}
