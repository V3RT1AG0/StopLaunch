package com.gamerequirements.Requirements;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.gamerequirements.ActivitySuperClass;
import com.gamerequirements.EndlessRecyclerView;
import com.gamerequirements.JSONCustom.CustomRequest;
import com.gamerequirements.JSONCustom.CustomVolleyRequest;
import com.gamerequirements.MyApplication;
import com.gamerequirements.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.codechimp.apprater.AppRater;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameListActivity extends ActivitySuperClass implements FloatingSearchView.OnQueryChangeListener
{
    /**
     * code for python server
     **/
    //private static final String gamelisturl = Singelton.getURL() + "loadlist";
    private static final String COUNT = "count";
    //private static final String gamelisturl = Singelton.getURL() + "index.php";
    private static final String gamelisturl = "http://192.168.1.9:5000/gameslist";
    List<Information> gamelist;
    GameListAdapter gameListAdapter;
    RecyclerView recyclerView;
    FloatingSearchView searchView;
    boolean doubleBackToExitPressedOnce = false;
    CircularProgressView progressView;
    Timer timer;
    LinearLayout errorlayout;
    SharedPreferences sharedPrefs;
    int curSize;
    Boolean nexttexnupdate = false;
    //JSONObject idListjsonObject=new JSONObject();
    ArrayList<String> idArrayList = new ArrayList<>();
    final List<Information> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        sharedPrefs = MyApplication.getContext().getSharedPreferences("com.gamerequirements", Context.MODE_PRIVATE);

        timer = new Timer();
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        progressView.startAnimation();
        gamelist = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_gamelist_recycler);
        LinearLayoutManager lmanager = new LinearLayoutManager(this);
        errorlayout = (LinearLayout) findViewById(R.id.errorlayout);
        findViewById(R.id.Conf).setVisibility(View.VISIBLE);
        findViewById(R.id.Share).setVisibility(View.VISIBLE);
        searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        searchView.setOnQueryChangeListener(this);
        searchView.setSearchFocused(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lmanager);

        VolleyOperation();

       recyclerView.addOnScrollListener(new EndlessRecyclerView(lmanager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                Log.d("Tag1", page + " " + totalItemsCount + " ");
                curSize = gameListAdapter.getItemCount();
                nexttexnupdate = true;
                VolleyOperation();
            }
        });
        Log.e("Error", gamelisturl);
        AppRater.app_launched(this);
    }

    void VolleyOperation()
    {

        HashMap<String, String> params = new HashMap<>();
        JSONArray jsonArray = new JSONArray(idArrayList);

        params.put("list", jsonArray.toString());
        Log.d("array",params.toString());
        CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.POST, gamelisturl, params, new Response.Listener<JSONObject>()
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
                errorlayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                errorlayout.findViewById(R.id.RetryButton).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Log.d("custom", "volley2");
                        errorlayout.setVisibility(View.GONE);
                        progressView.setVisibility(View.VISIBLE);
                        VolleyOperation();
                    }
                });
                Log.d("Error", error.toString());
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(GameListActivity.this, "Please check your connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

      /* jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); */
        RequestQueue requestqueue = CustomVolleyRequest.getInstance(this.getApplicationContext()).getRequestQueue();
        requestqueue.add(jsonObjectRequest);
    }

    void handleresponse(JSONObject response)
    {
        try
        {
            JSONArray result = response.getJSONArray("result");
            Log.d("TAG", response.toString());
            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jsonObject = result.getJSONObject(i);
                int gid = jsonObject.getInt("gid");
                //String summary = new String(jsonObject.getString("summary").getBytes("ISO-8859-1"), "UTF-8");
                String summary = String.valueOf(Html.fromHtml(jsonObject.getString("summary")));
                //EntityUtils.toString(res, HTTP.UTF_8);
                String genre = jsonObject.getString("genre");
                String date = jsonObject.getString("date");
                String name = jsonObject.getString("gname");
                // idarray.add(Integer.toString(gid));
                idArrayList.add(Integer.toString(gid));
                gamelist.add(new Information(gid, name, summary, genre, date));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {

            errorlayout.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);

            if (nexttexnupdate)   //add 10 more data
            {
                gameListAdapter.notifyItemRangeInserted(curSize, gamelist.size() - 1);
                nexttexnupdate = false;
            } else
            {
                recyclerView.setVisibility(View.VISIBLE);
                gameListAdapter = new GameListAdapter(gamelist);
                recyclerView.setAdapter(gameListAdapter);
            }
            /*SharedPreferences.Editor editor = sharedPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(gamelist);
            editor.putString("database", json);
            editor.putInt("dbv",Singelton.getDatabaseversion());
            editor.commit();*/
        }
    }


    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery)
    {
        if (newQuery.equals(""))
        {
            VolleyOperation();
            return;
        }
        newQuery = newQuery.toString().toLowerCase();
        if (timer != null)
            timer.cancel();
        final String finalNewQuery = newQuery;
        timer = new Timer();
        timer.schedule(new TimerTask()
                       {
                           @Override
                           public void run()
                           {

                               VolleySearchRequest(finalNewQuery);
                        /*for (int j = 0; j < gamelist.size(); j++)
                        {
                            final String text = gamelist.get(j).title.toLowerCase();
                            if (text.contains(finalNewQuery))
                            {
                                filteredList.add(gamelist.get(j));
                            }
                        }

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                recyclerView.setLayoutManager(new LinearLayoutManager(GameListActivity.this));
                                gameListAdapter = new GameListAdapter(filteredList);
                                recyclerView.setAdapter(gameListAdapter);
                                gameListAdapter.notifyDataSetChanged();
                            }
                        });*/

                           }
                       },
                200
        );


    }


    void VolleySearchRequest(final String query)
    {
        RequestQueue requestqueue = CustomVolleyRequest.getInstance(this.getApplicationContext()).getRequestQueue();
        requestqueue.cancelAll("search");
        String searchurl = "http://192.168.1.9:5000/gameslist/search/" + query;
        CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.GET, searchurl, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                handleSearchResponse(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressView.setVisibility(View.GONE);
                errorlayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                   errorlayout.findViewById(R.id.RetryButton).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Log.d("custom","volley2");
                            errorlayout.setVisibility(View.GONE);
                            progressView.setVisibility(View.VISIBLE);
                            searchView.clearQuery();
                            VolleyOperation();
                        }
                    });
                Log.d("Error", error.toString());
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(GameListActivity.this, "Please check your connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        jsonObjectRequest.setTag("search");
        requestqueue.add(jsonObjectRequest);

    }

    private void handleSearchResponse(JSONObject response)
    {


        JSONArray result;
        try
        {
            filteredList.clear();
            result = response.getJSONArray("result");
            Log.d("TAG", response.toString());
            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jsonObject = result.getJSONObject(i);
                int gid = jsonObject.getInt("gid");
                String summary = String.valueOf(Html.fromHtml(jsonObject.getString("summary")));
                String genre = jsonObject.getString("genre");
                String date = jsonObject.getString("date");
                String name = jsonObject.getString("gname");
                filteredList.add(new Information(gid, name, summary, genre, date));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {
            errorlayout.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(GameListActivity.this));
            gameListAdapter = new GameListAdapter(filteredList);
            recyclerView.setAdapter(gameListAdapter);
            gameListAdapter.notifyDataSetChanged();
        }

    }


}
