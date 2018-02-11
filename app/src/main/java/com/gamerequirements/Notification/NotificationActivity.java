package com.gamerequirements.Notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gamerequirements.JSONCustom.CustomRequest;
import com.gamerequirements.JSONCustom.CustomVolleyRequest;
import com.gamerequirements.MyApplication;
import com.gamerequirements.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity
{
    List<OuterCardInformation> info = new ArrayList<>();

    String notificationurl;
    RecyclerView my_recycler_view;
    OuterAdapter adapter;
    CircularProgressView progressView;
    SharedPreferences sharedPrefs;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        sharedPrefs = MyApplication.getContext().getSharedPreferences("com.gamerequirements", Context.MODE_PRIVATE);
        progressView = findViewById(R.id.progress_view);
        progressView.startAnimation();
        if (MyApplication.getURL() == null)
            MyApplication.setURL(sharedPrefs.getString("url", null));
        notificationurl= MyApplication.getURL() + "newgames";
        my_recycler_view = findViewById(R.id.outerRecycler);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        VolleyRequest();
    }

    private void VolleyRequest()
    {
        CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.GET, notificationurl, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                handleresponse(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressView.setVisibility(View.GONE);
                Log.d("TAG", error.toString());
            }
        });
        RequestQueue requestqueue = CustomVolleyRequest.getInstance(this.getApplicationContext()).getRequestQueue();
        requestqueue.add(jsonObjectRequest);
    }


    void handleresponse(JSONObject response)
    {
        try
        {
            JSONArray result = response.getJSONArray("newgame");
            Log.d("TAG", response.toString());
            count = result.length();
            for (int i = 0; i < result.length(); i++)
            {
                List<InnerCardInformation> innerInfo = new ArrayList<>();
                JSONObject jsonObject = result.getJSONObject(i);
                String data = jsonObject.getString("date");
                JSONArray innerDataArray = jsonObject.getJSONArray("gamelist");
                String count = String.valueOf(innerDataArray.length());
                for (int j = 0; j < innerDataArray.length(); j++)
                {
                    //Log.d("TAG", innerDataArray.toString());
                    JSONObject innerjsonObject = innerDataArray.getJSONObject(j);
                    String gid = innerjsonObject.getString("gid");
                    String name = innerjsonObject.getString("gname");
                    Log.d("TAG1", gid +" "+ name);
                    innerInfo.add(new InnerCardInformation(gid, name));
                }
                Log.d("TAG2", data +" "+ count + "  " + innerInfo.get(0).name);
                info.add(new OuterCardInformation(data, innerInfo, count));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {
            progressView.setVisibility(View.GONE);
            adapter = new OuterAdapter(info);
            my_recycler_view.setAdapter(adapter);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putInt("count", count);
            editor.commit();
            Log.d("TAG", "finally");
        }
    }

    public void back(View view)
    {
        finish();
    }
}
