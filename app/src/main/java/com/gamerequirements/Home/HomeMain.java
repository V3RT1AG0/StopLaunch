package com.gamerequirements.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.gamerequirements.Blog.Information;
import com.gamerequirements.JSONCustom.CustomVolleyRequest;
import com.gamerequirements.MyApplication;
import com.gamerequirements.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeMain extends Fragment
{

    private final String latestBlogPostsUrl = MyApplication.getBlogUrl() + "/wp-json/wp/v2/posts";
    List<Information> bloglist;  /**This Information class is taken from Blog package**/
    BlogAdapter blogAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager lmanager;
    private final String blogUrl = MyApplication.getBlogUrl()+ "wp-json/wp/v2/posts?_embed=true";

    public HomeMain()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_home_main, container, false);
        return (v);
    }

    public static HomeMain newInstance()
    {
        HomeMain homeMain = new HomeMain();
        return homeMain;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        bloglist = new ArrayList<>();
        recyclerView = getActivity().findViewById(R.id.home_blog_recycler);
        lmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        blogAdapter = new BlogAdapter(bloglist, this.getLifecycle());
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lmanager);
        recyclerView.setAdapter(blogAdapter);

        VolleyOperation();
    }

    void VolleyOperation()
    {
        JsonArrayRequest jsonarrayrequest = new JsonArrayRequest(Request.Method.GET, blogUrl, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                handleresponse(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });
        RequestQueue requestqueue = CustomVolleyRequest.getInstance(getActivity()).getRequestQueue();
        requestqueue.add(jsonarrayrequest);
    }

    void handleresponse(JSONArray response)
    {
        try
        {

            Log.d("TAG", response.toString());
            for (int i = 0; i < response.length(); i++)
            {
                JSONObject jsonObject = response.getJSONObject(i);
                int id = jsonObject.getInt("id");
                int category = jsonObject.getJSONArray("categories").getInt(0);
                String title = jsonObject.getJSONObject("title").getString("rendered");
                String content = jsonObject.getJSONObject("content").getString("rendered");
                String subtitle = jsonObject.getJSONObject("excerpt").getString("rendered");
                String videoimgurl;
                Log.d("videoimageurl", title + category);
                if (category == 5)
                {
                    Log.d("videoimageurl", content.indexOf("[") + "");
                    videoimgurl = content.substring(content.indexOf("[") + 1, content.indexOf("]"));
                } else
                {
                    Log.d("videoimageurl", jsonObject.getJSONObject("_embedded").toString());
                    videoimgurl = jsonObject.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).getJSONObject("media_details").getJSONObject("sizes").getJSONObject("medium").getString("source_url");
                }
                bloglist.add(new Information(id, title, subtitle, videoimgurl, category));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {

            recyclerView.setVisibility(View.VISIBLE);
            blogAdapter.notifyDataSetChanged();

        }
    }


}

