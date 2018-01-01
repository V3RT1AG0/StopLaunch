package com.gamerequirements.Notification;

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
import com.gamerequirements.R;
import com.gamerequirements.Singelton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity
{
    List<OuterCardInformation> info = new ArrayList<>();
    List<InnerCardInformation> innerInfo = new ArrayList<>();
    private static final String notificationurl = Singelton.getURL() + "newgames";
    RecyclerView my_recycler_view;
    OuterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        VolleyRequest();
        my_recycler_view = (RecyclerView) findViewById(R.id.outerRecycler);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jsonObject = result.getJSONObject(i);
                String data = jsonObject.getString("date");
                JSONArray innerDataArray = jsonObject.getJSONArray("gamelist");
                String count = String.valueOf(innerDataArray.length());
                for (int j = 0; j < innerDataArray.length(); j++)
                {
                    JSONObject innerjsonObject = innerDataArray.getJSONObject(j);
                    String gid = innerjsonObject.getString("gid");
                    String name = innerjsonObject.getString("gname");
                    innerInfo.add(new InnerCardInformation(gid, name));
                }
                info.add(new OuterCardInformation(data, innerInfo, count));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {
            adapter = new OuterAdapter(info);
            my_recycler_view.setAdapter(adapter);
            Log.d("TAG", "finally");
        }
    }

    public void back(View view)
    {
        finish();
    }
}
