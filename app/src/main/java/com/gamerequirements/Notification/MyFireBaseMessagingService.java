package com.gamerequirements.Notification;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gamerequirements.MyApplication;
import com.gamerequirements.R;
import com.gamerequirements.TabbedActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MyFireBaseMessagingService extends FirebaseMessagingService
{

    private static final String TAG = "MyFirebaseMsgService";
    Map mymap;
    int value;
    String key;
    Intent intent;
    Random rand;
    String channelId = "HeadsUpNotification";
    NotificationManager notificationManager;
    final static String KEY = "notificationCount";
    int count=0;
    Set<String> sets=null;
    SharedPreferences sharedPrefs;
    final static String SETKEY = "notificationSet";

    @Override
    public void onCreate()
    {
        super.onCreate();

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        sharedPrefs = MyApplication.getContext().getSharedPreferences("com.gamerequirements", Context.MODE_PRIVATE);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        //Displaying data in log
        //It is optional
        rand = new Random();
        sets = sharedPrefs.getStringSet(SETKEY, null);
        if (sets == null)
            sets = new LinkedHashSet<>();
        count = sharedPrefs.getInt(KEY, 0);
        // Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        mymap = remoteMessage.getData();
        //Log.d("mymap", mymap.toString());
        try
        {
            if (mymap.containsKey("update"))
            {
                value = Integer.parseInt(mymap.get("update").toString());
                key = "update";
                intent = new Intent(this, NotificationActivity.class);
                sendNotification(remoteMessage.getNotification().getBody(), value, key);
                //This is a notification message : notification message with payload
            } else if (mymap.containsKey("postID"))
            {
                intent = new Intent(this, TabbedActivity.class).putExtra("slide", 1);
                key = "postID";
                Log.d("payload",mymap.get("title").toString());
                value = Integer.parseInt(mymap.get("postID").toString());
                //sendNotification(remoteMessage.getNotification().getBody(), value, key);
                sendNotification(mymap.get("title").toString(), value, key);
                //This is only payload message without a body
            } else
            {
                sendPlainNotification(remoteMessage.getNotification().getBody());
            }
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }


    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody, int value, String key)
    {
        //Intent intent = new Intent(this, FloatingPost.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(key, value);
        /**this intent is only used when application is running and the given extras are provided.
         * ELSE if the application is closed the Splash screen will receive intent directly from Firebase and not frome here
         * Application ON:Firebase-This Service with extras from firebase-ACtivity specified by pendingintent with extras specified in this service
         * Application Closed:Firebase-Splash Screen with extras from firebase-Activity specified in Splash
         */
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        Intent cancelIntent = new Intent(this, NotificationBroadCastReceiver.class);
        cancelIntent.setAction("com.gr.notification_cancelled");
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0,cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // Adds the back stack
        //stackBuilder.addParentStack(NotificationActivity.class);
        // stackBuilder.addNextIntent(intent);
        // Adds the Intent to the top of the stack
        ;
// Gets a PendingIntent containing the entire back stack
        Log.d("NotificationCheck", "data notification count" + count);
        sets.add(messageBody);
        NotificationCompat.Builder notificationBuilder;
        if (count == 0)
        {

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setGroup("Group")
                    .setDeleteIntent(cancelPendingIntent)
                    .setContentIntent(pendingIntent);
            Log.d("NotificationCheck", "data notification1");
        } else
        {
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.InboxStyle inboxStyle= new NotificationCompat.InboxStyle();
            for (String message:sets)
            {
                inboxStyle.addLine(message);
            }
            inboxStyle.setBigContentTitle(sets.size()+" new articles for you!");

            notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setStyle(inboxStyle)
                    .setSound(defaultSoundUri)
                    .setGroup("Group")
                    .setDeleteIntent(cancelPendingIntent)
                    .setContentIntent(pendingIntent);
            Log.d("NotificationCheck", "data notification2");
        }
        count++;
        sharedPrefs.edit().putInt(KEY, count).commit();
        sharedPrefs.edit().putStringSet(SETKEY,sets).commit();
        Log.d("NotificationCheck", "data notification");

        showNotification(notificationBuilder,94182);
    }


    void sendPlainNotification(String messageBody)
    {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
        Log.d("NotificationCheck", "plain notification");
        showNotification(notificationBuilder,94181);
    }

    void showNotification(NotificationCompat.Builder notificationBuilder,int notifyID)
    {


        //int notifyID = rand.nextInt(100) + 1;
        //int notifyID = 9418;
        Log.d("NotificationCheck", "Ejected Notification");
        if (notificationManager != null)
        {
            notificationManager.notify(notifyID, notificationBuilder.build());
        }
    }
}


