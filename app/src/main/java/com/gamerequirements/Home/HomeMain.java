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
    private static Runnable SCROLLING_RUNNABLE;
    private static int position = 0;
    String date = null;
    static Handler mHandler;
    static BlogAdapter blogAdapter;
    static RecyclerView recyclerView;
    static LinearLayoutManager lmanager;
    private final String blogUrl = MyApplication.getBlogUrl() + "wp-json/wp/v2/posts?_embed=true&orderby=id&page=1&fields=id,title,date,excerpt,acf,categories,tags,_embedded.wp:featuredmedia&per_page=4";
    private final String gamesStatusURL = MyApplication.getURL() + "getLastInsertedGameCount";
    CircularProgressView progressView;
    View v;


    public HomeMain()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_home_main, container, false);
        progressView = v.findViewById(R.id.progress_view_blog);
        progressView.startAnimation();
        bloglist = new ArrayList<>();
        recyclerView = v.findViewById(R.id.home_blog_recycler);
        lmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        blogAdapter = new BlogAdapter(bloglist, this.getLifecycle());
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lmanager);
        recyclerView.setAdapter(blogAdapter);
this.v = v;

        v.findViewById(R.id.gameInfoCard).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((TabbedActivity) getActivity()).getmViewPager().setCurrentItem(2);
            }
        });


        v.findViewById(R.id.View_all_config).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), MainActivityConfig.class));
            }
        });

        v.findViewById(R.id.view_all_articles).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((TabbedActivity) getActivity()).getmViewPager().setCurrentItem(1);
            }
        });
      /* DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);*/


        mHandler = new Handler(Looper.getMainLooper());
        final int duration = 7000;
        SCROLLING_RUNNABLE = new Runnable()
        {
            @Override
            public void run()
            {
                Log.d("duration", "called"+position);
                if (++position == bloglist.size())
                    position = 0;
                recyclerView.smoothScrollToPosition(position);
                //recyclerView.smoothScrollBy(pixelsToMove, 0);
                mHandler.postDelayed(SCROLLING_RUNNABLE, duration);
            }
        };

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        setUpSlider();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        cancelSlider();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        System.out.println("Scroll Settling");
                        break;

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                position = lmanager.findLastVisibleItemPosition();
            }
        });

        /*recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
            {
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e)
            {
                Log.d("touchevenr", String.valueOf(e.getAction()));
                if (e.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Log.d("touchevenr", "down");
                    cancelSlider();
                } else if (e.getAction() == MotionEvent.ACTION_UP)
                {
                    setUpSlider();
                    Log.d("touchevenr", "down");
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
            {

            }
        });*/


        VolleyOperation();
        volleyRequestForGamesCount();
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



    }

    static void cancelSlider()
    {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.removeCallbacks(SCROLLING_RUNNABLE);
    }

    static void setUpSlider()
    {
        mHandler.removeCallbacksAndMessages(null);
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
            TextView count = v.findViewById(R.id.games_count);
            TextView last_updated_TV = v.findViewById(R.id.last_updated);
            TextView last_added_games_count = v.findViewById(R.id.new_games_added);
            final LinearLayout newgamesAddedLL = v.findViewById(R.id.new_games_added_LL);
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


    void displayEnabledConfig()
    {
        SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(MyApplication.getSharedPrefrenceKey(), Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("StoredConfigEnabled", false))
        {
            String CPUname = sharedPreferences.getString("CPUname", null),
                    GPUname = sharedPreferences.getString("GPUname", null),
                    RAMname = sharedPreferences.getString("RAMname", null);
            TextView cpu = v.findViewById(R.id.CPU_text);
            cpu.setText(CPUname);
            TextView gpu = v.findViewById(R.id.GPU_text);
            gpu.setText(GPUname);
            TextView ram = v.findViewById(R.id.RAM_text);
            ram.setText(RAMname);
            v.findViewById(R.id.noConfig).setVisibility(View.GONE);
            v.findViewById(R.id.configLL).setVisibility(View.VISIBLE);
        } else
        {
            v.findViewById(R.id.noConfig).setVisibility(View.VISIBLE);
            v.findViewById(R.id.configLL).setVisibility(View.GONE);
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
                error.printStackTrace();
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

            Log.d("TAGMAINSLIDER", response.toString());
            for (int i = 0; i < response.length(); i++)
            {
                JSONObject jsonObject = response.getJSONObject(i);
                int id = jsonObject.getInt("id");
                int category = jsonObject.getJSONArray("categories").getInt(0);
                String title = jsonObject.getJSONObject("title").getString("rendered");
                if (date == null)
                {
                    date = jsonObject.getString("date");
                    TextView textView = v.findViewById(R.id.date);
                    // textView.setText(DateTimeUtil.formatToYesterdayOrToday(date)); //TODO remove this if block if no longer required
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
                    videoimgurl = jsonObject.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).getJSONObject("media_details").getJSONObject("sizes").getJSONObject("medium_large").getString("source_url");
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
            setUpSlider();
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

