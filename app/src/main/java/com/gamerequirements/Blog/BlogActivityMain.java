package com.gamerequirements.Blog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.gamerequirements.EndlessRecyclerView;
import com.gamerequirements.JSONCustom.CustomVolleyRequest;
import com.gamerequirements.MyApplication;
import com.gamerequirements.R;
import com.gamerequirements.Singelton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BlogActivityMain extends Fragment
{
    String blogUrl;
    List<Information> bloglist;
    BlogAdapter blogAdapter;
    RecyclerView recyclerView;
    EndlessRecyclerView endlessRecyclerView;
    CircularProgressView progressView;
    LinearLayout errorlayout;
    LinearLayoutManager lmanager;
    int curSize;
    Boolean nexttexnupdate = false;
    SparseArray cats, tags;

    public static BlogActivityMain newInstance()
    {
        BlogActivityMain blogActivityMain = new BlogActivityMain();
        return blogActivityMain;
    }

    public BlogActivityMain()
    {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        blogUrl = MyApplication.getBlogUrl() + "wp-json/wp/v2/posts?_embed=true&orderby=id&fields=id,title,acf,excerpt,categories,tags,_embedded.wp:featuredmedia&page=";

        cats = new SparseArray();
        tags = new SparseArray();

        progressView = getActivity().findViewById(R.id.progress_view2);
        progressView.startAnimation();
        bloglist = new ArrayList<>();
        recyclerView = getActivity().findViewById(R.id.blog_recycler);
        lmanager = new LinearLayoutManager(getActivity());
        errorlayout = getActivity().findViewById(R.id.errorlayout2);
        blogAdapter = new BlogAdapter(bloglist, this.getLifecycle(),cats,tags);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lmanager);
        recyclerView.setAdapter(blogAdapter);
        AddOnScrollListenrerToRecyclerView();
        getTagsAndCategories();
    }

    private void getTagsAndCategories()
    {
        String categoryUrl = MyApplication.getBlogUrl() + "wp-json/wp/v2/categories?fields=id,name";

        VolleyOperation(categoryUrl, "cats");


    }

    private void AddOnScrollListenrerToRecyclerView()
    {
        endlessRecyclerView = new EndlessRecyclerView(lmanager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                Log.d("Tag1", page + " " + totalItemsCount + " ");
                curSize = blogAdapter.getItemCount();
                nexttexnupdate = true;
                VolleyOperation(page + 1);
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerView);
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_blog_main, container, false);
        return (v);
    }

    private void VolleyOperation(String url, final String type)
    {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                handleresponse(response,type);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressView.setVisibility(View.GONE);
                errorlayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                resetRecyclerViewData();
                errorlayout.findViewById(R.id.RetryButton2).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Log.d("custom", "volley2");
                        errorlayout.setVisibility(View.GONE);
                        progressView.setVisibility(View.VISIBLE);
                        VolleyOperation(1);
                    }
                });
                Log.d("Error", error.toString());
                if (error instanceof NoConnectionError)
                {
                    TextView textView = errorlayout.findViewById(R.id.errorMessage2);
                    textView.setText("Check your connection and try again");
                    //  Toast.makeText(GameListActivity.this, "Please check your connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        arrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestqueue = CustomVolleyRequest.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        requestqueue.add(arrayRequest);
    }


    private void VolleyOperation(int offset)
    {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, blogUrl + offset, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                handleresponse(response,"blog");
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                try
                {
                    // if request is made for a page which does not have a data do not do anything. So simply return on status code 400
                    if (error.networkResponse.statusCode == 400)
                        return;
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                progressView.setVisibility(View.GONE);
                errorlayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                resetRecyclerViewData();
                errorlayout.findViewById(R.id.RetryButton2).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Log.d("custom", "volley2");
                        errorlayout.setVisibility(View.GONE);
                        progressView.setVisibility(View.VISIBLE);
                        VolleyOperation(1);
                    }
                });
                Log.d("Error", error.toString());
                if (error instanceof NoConnectionError)
                {
                    TextView textView = errorlayout.findViewById(R.id.errorMessage2);
                    textView.setText("Check your connection and try again");
                    //  Toast.makeText(GameListActivity.this, "Please check your connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        arrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestqueue = CustomVolleyRequest.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        requestqueue.add(arrayRequest);
    }

    private void resetRecyclerViewData()
    {
        bloglist.clear();
        blogAdapter.notifyDataSetChanged();
        endlessRecyclerView.resetState();
        nexttexnupdate = false;
    }

    void handleresponse(JSONArray response, String type)
    {
        try
        {
            //JSONArray result = response.getJSONArray("result");
            Log.d("TAG", response.toString());
            Log.d("catsTags", cats+""+tags);
            switch (type)
            {
                case "blog":
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        int category = jsonObject.getJSONArray("categories").getInt(0);
                        JSONArray tags = jsonObject.getJSONArray("tags");
                        String title = jsonObject.getJSONObject("title").getString("rendered");
                        String subtitle = jsonObject.getJSONObject("excerpt").getString("rendered");
                        String videoimgurl;
                        Log.d("videoimageurl", title + category);
                        if (category == 5)
                        {
                            //String content = jsonObject.getJSONObject("content").getString("rendered");
                            //Log.d("videoimageurl", content.indexOf("[") + "");
                            //videoimgurl = content.substring(content.indexOf("[") + 1, content.indexOf("]"));
                            videoimgurl = jsonObject.getJSONObject("acf").getString("YTembed");
                        } else
                        {
                            Log.d("videoimageurl", jsonObject.getJSONObject("_embedded").toString());
                            videoimgurl = jsonObject.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).getJSONObject("media_details").getJSONObject("sizes").getJSONObject("medium").getString("source_url");
                        }
                        bloglist.add(new Information(id, title, subtitle, videoimgurl, category,tags));
                        errorlayout.setVisibility(View.GONE);
                        progressView.setVisibility(View.GONE);

                        if (nexttexnupdate)
                        {
                            blogAdapter.notifyItemRangeInserted(curSize, bloglist.size() - 1);
                            nexttexnupdate = false;
                        } else
                        {
                            recyclerView.setVisibility(View.VISIBLE);
                            blogAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case "cats":
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        cats.put(jsonObject.getInt("id"),jsonObject.getString("name"));
                    }
                    String tagsUrl = MyApplication.getBlogUrl() + "wp-json/wp/v2/tags?fields=id,name";
                    VolleyOperation(tagsUrl, "tags");
                    break;
                case "tags":

                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        tags.put(jsonObject.getInt("id"),jsonObject.getString("name"));
                    }
                    Log.d("catsTags2", tags.toString());
                    VolleyOperation(1);
                    break;
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        Log.d("visibility", String.valueOf(visible)+Singelton.getYouTubePlayer());
        if (!visible && Singelton.getYouTubePlayer() != null)
            Singelton.getYouTubePlayer().pause();
    }
}

