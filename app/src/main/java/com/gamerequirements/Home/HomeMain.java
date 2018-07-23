package com.gamerequirements.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.gamerequirements.Blog.Information;
import com.gamerequirements.JSONCustom.CustomRequest;
import com.gamerequirements.JSONCustom.CustomVolleyRequest;
import com.gamerequirements.MyApplication;
import com.gamerequirements.Notification.NotificationActivity;
import com.gamerequirements.R;
import com.gamerequirements.Requirements.GameListActivity;
import com.gamerequirements.SaveCofig.MainActivityConfig;
import com.gamerequirements.Singelton;
import com.gamerequirements.TabbedActivity;
import com.gamerequirements.Utils.DateTimeUtil;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeMain extends Fragment
{

    private final String latestBlogPostsUrl = MyApplication.getBlogUrl() + "/wp-json/wp/v2/posts";
    List<Information> bloglist;
    static RecyclerView.OnScrollListener onScrollListener;
    /**
     * This Information class is taken from Blog package
     **/
    private static int width;
    String date = null;
    static Handler mHandler;
    static Runnable SCROLLING_RUNNABLE;
    static BlogAdapter blogAdapter;
    static RecyclerView recyclerView;
    static LinearLayoutManager lmanager;
    private final String blogUrl = MyApplication.getBlogUrl() + "wp-json/wp/v2/posts?_embed=true&orderby=id&page=1&fields=id,title,date_gmt,excerpt,acf,categories,tags,_embedded.wp:featuredmedia&per_page=4";
    private final String gamesStatusURL = MyApplication.getURL() + "getLastInsertedGameCount";
    CircularProgressView progressView;

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

        progressView = getActivity().findViewById(R.id.progress_view_blog);
        progressView.startAnimation();
        bloglist = new ArrayList<>();
        recyclerView = getActivity().findViewById(R.id.home_blog_recycler);
        lmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        blogAdapter = new BlogAdapter(bloglist, this.getLifecycle());
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lmanager);
        recyclerView.setAdapter(blogAdapter);

        getActivity().findViewById(R.id.gameInfoCard).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((TabbedActivity) getActivity()).getmViewPager().setCurrentItem(2);
            }
        });


        getActivity().findViewById(R.id.View_all_config).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), MainActivityConfig.class));
            }
        });

        getActivity().findViewById(R.id.view_all_articles).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((TabbedActivity) getActivity()).getmViewPager().setCurrentItem(1);
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        setUpSlider();
        VolleyOperation();
        volleyRequestForGamesCount();
    }

    static void setUpSlider()
    {


        final int duration = 2000;
        final int pixelsToMove = width - 8; //1300;
        //Log.d("width",recyclerView.getMeasuredWidth()+""+recyclerView.getWidth()+" "+width);
        mHandler = new Handler(Looper.getMainLooper());
        SCROLLING_RUNNABLE = new Runnable()
        {

            @Override
            public void run()
            {
                recyclerView.smoothScrollBy(pixelsToMove, 0);
                mHandler.postDelayed(this, duration);
            }
        };
        onScrollListener = new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = lmanager.findLastCompletelyVisibleItemPosition();
                if (lastItem == lmanager.getItemCount() - 1)
                {
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            recyclerView.scrollToPosition(0);
                            //  recyclerView.setAdapter(null);
                            //recyclerView.setAdapter(blogAdapter);
                            mHandler.postDelayed(SCROLLING_RUNNABLE, 7000);
                        }
                    }, 7000);
                }
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);
        mHandler.postDelayed(SCROLLING_RUNNABLE, 7000);
    }

    private void volleyRequestForGamesCount()
    {
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, gamesStatusURL, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                handleGamesStatusResponse(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });
        RequestQueue requestqueue = CustomVolleyRequest.getInstance(getActivity()).getRequestQueue();
        requestqueue.add(customRequest);
    }

    private void handleGamesStatusResponse(JSONObject response)
    {

        try
        {
            final SharedPreferences sharedPrefs = MyApplication.getContext().getSharedPreferences("com.gamerequirements", Context.MODE_PRIVATE);
            JSONObject obj = response.getJSONObject("result");
            int last_added_game_count = obj.getInt("last_added_game_count");
            final String last_updated = obj.getString("last_inserted_date");
            String totol_game_count = obj.getString("total_game_count");
            GameListActivity.searchView.setSearchHint("Search from " + totol_game_count + " titles");
            TextView count = getActivity().findViewById(R.id.games_count);
            TextView last_updated_TV = getActivity().findViewById(R.id.last_updated);
            TextView last_added_games_count = getActivity().findViewById(R.id.new_games_added);
            final LinearLayout newgamesAddedLL = getActivity().findViewById(R.id.new_games_added_LL);
            count.setText(totol_game_count);
            last_updated_TV.setText("Last updated: " + last_updated);
            last_added_games_count.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    sharedPrefs.edit().putString("lastUpdated", last_updated).commit();
                    newgamesAddedLL.setVisibility(View.GONE);
                    startActivity(new Intent(getActivity(), NotificationActivity.class));
                }
            });

            String storedlasUpdated = sharedPrefs.getString("lastUpdated", "0");
            if (storedlasUpdated.equals(last_updated))
                return;

            if (last_added_game_count == 0)
                last_added_games_count.setText(last_added_game_count + " game added");
            else
                last_added_games_count.setText(last_added_game_count + " games added");
            newgamesAddedLL.setVisibility(View.VISIBLE);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("onresume", "refreshed");

        displayEnabledConfig();
    }

    public static void cancelSlider()
    {
        mHandler.removeCallbacks(SCROLLING_RUNNABLE);
        recyclerView.removeOnScrollListener(onScrollListener);
    }

    void displayEnabledConfig()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyApplication.getSharedPrefrenceKey(), Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("StoredConfigEnabled", false))
        {
            String CPUname = sharedPreferences.getString("CPUname", null),
                    GPUname = sharedPreferences.getString("GPUname", null),
                    RAMname = sharedPreferences.getString("RAMname", null);
            TextView cpu = getActivity().findViewById(R.id.CPU_text);
            cpu.setText(CPUname);
            TextView gpu = getActivity().findViewById(R.id.GPU_text);
            gpu.setText(GPUname);
            TextView ram = getActivity().findViewById(R.id.RAM_text);
            ram.setText(RAMname);
            getActivity().findViewById(R.id.noConfig).setVisibility(View.GONE);
            getActivity().findViewById(R.id.configLL).setVisibility(View.VISIBLE);
        } else
        {
            getActivity().findViewById(R.id.noConfig).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.configLL).setVisibility(View.GONE);
        }
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
                progressView.setVisibility(View.GONE);
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
                if (date == null)
                {
                    date = jsonObject.getString("date_gmt");
                    TextView textView = getActivity().findViewById(R.id.date);
                    textView.setText(DateTimeUtil.formatToYesterdayOrToday(date));
                }
                // String content = jsonObject.getJSONObject("content").getString("rendered");
                String subtitle = jsonObject.getJSONObject("excerpt").getString("rendered");
                String videoimgurl;
                Log.d("videoimageurl", title + category);
                if (category == 5)
                {
                    // Log.d("videoimageurl", content.indexOf("[") + "");
                    // videoimgurl = content.substring(content.indexOf("[") + 1, content.indexOf("]"));
                    videoimgurl = jsonObject.getJSONObject("acf").getString("YTembed");
                } else if (category == 2)
                {
                    Log.d("videoimageurl", jsonObject.getJSONObject("_embedded").toString());
                    videoimgurl = jsonObject.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).getJSONObject("media_details").getJSONObject("sizes").getJSONObject("medium").getString("source_url");
                } else
                    continue;
                bloglist.add(new Information(id, title, subtitle, videoimgurl, category));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {
            progressView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            blogAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void setMenuVisibility(final boolean visible)
    {
        super.setMenuVisibility(visible);
        Log.d("visibility2", String.valueOf(visible) + Singelton.getYouTubePlayer());
        if (!visible && Singelton.getYouTubePlayer() != null)
            Singelton.getYouTubePlayer().pause();
    }


}

