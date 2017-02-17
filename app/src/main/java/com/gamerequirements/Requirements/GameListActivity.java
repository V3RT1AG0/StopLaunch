package com.gamerequirements.Requirements;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gamerequirements.ActivitySuperClass;
import com.gamerequirements.JSONCustom.CustomVolleyRequest;
import com.gamerequirements.R;
import com.gamerequirements.Singelton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameListActivity extends ActivitySuperClass implements TextWatcher, FloatingSearchView.OnQueryChangeListener
{
    /**code for python server**/
    //private static final String gamelisturl = Singelton.getURL() + "loadlist";
    private static final String gamelisturl = Singelton.getURL() + "index.php";
    List<Information> gamelist;
    GameListAdapter gameListAdapter;
    RecyclerView recyclerView;
    EditText searcheditText;
    FloatingSearchView searchView;
    boolean doubleBackToExitPressedOnce = false;
    CircularProgressView progressView;
    Timer timer;
    LinearLayout errorlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        timer = new Timer();
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        progressView.startAnimation();
        gamelist = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_gamelist_recycler);
        //searcheditText= (EditText) findViewById(R.id.search_TV);
        //searcheditText.addTextChangedListener(this);
        errorlayout = (LinearLayout)findViewById(R.id.errorlayout);

        searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        searchView.setOnQueryChangeListener(this);
        searchView.setSearchFocused(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.e("Error", gamelisturl);
        VolleyOperation();
    }

    /**code for python server**/
/*
    void VolleyOperation()
    {
        JsonArrayRequest jsonarrayrequest = new JsonArrayRequest(Request.Method.GET, gamelisturl, null, new Response.Listener<JSONArray>()
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
                Log.e("Error", error.toString());
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(GameListActivity.this, "Please check your connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
        RequestQueue requestqueue = CustomVolleyRequest.getInstance(this.getApplicationContext()).getRequestQueue();
        requestqueue.add(jsonarrayrequest);
    }

    void handleresponse(JSONArray response)
    {
        try
        {
            Log.d("TAG", response.toString());
            for (int i = 0; i < response.length(); i++)
            {

                JSONArray jarr = response.getJSONArray(i);
                int id = jarr.getInt(1);
                String summary = jarr.getString(2).replace("\n", ",");
                ;
                String genre = jarr.getString(3).replace("\n", ",");
                String date = jarr.getString(4);
                String name = jarr.getString(6);
                gamelist.add(new Information(id, name, summary, genre, date));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {
            progressView.setVisibility(View.GONE);
            gameListAdapter = new GameListAdapter(gamelist);
            recyclerView.setAdapter(gameListAdapter);

        }
    }*/

    void VolleyOperation()
    {
        Log.d("custom","volley");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, gamelisturl, null, new Response.Listener<JSONObject>()
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
                        Log.d("custom","volley2");
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

       jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestqueue = CustomVolleyRequest.getInstance(this.getApplicationContext()).getRequestQueue();
        requestqueue.add(jsonObjectRequest);
    }

    void handleresponse(JSONObject response)
    {
        try
        {
            JSONArray result=response.getJSONArray("result");
            Log.d("TAG", response.toString());
            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jsonObject = result.getJSONObject(i);
                int gid = jsonObject.getInt("gid");
                String summary = jsonObject.getString("summary");
                String genre = jsonObject.getString("genre");
                String date = jsonObject.getString("date");
                String name = jsonObject.getString("gname");
                gamelist.add(new Information(gid, name, summary, genre, date));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {
            recyclerView.setVisibility(View.VISIBLE);
            errorlayout.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);
            gameListAdapter = new GameListAdapter(gamelist);
            recyclerView.setAdapter(gameListAdapter);

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        //searcheditText.getText().clear();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        charSequence = charSequence.toString().toLowerCase();
        final List<Information> filteredList = new ArrayList<>();

        for (int j = 0; j < gamelist.size(); j++)
        {

            final String text = gamelist.get(j).title.toLowerCase();
            if (text.contains(charSequence))
            {

                filteredList.add(gamelist.get(j));
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gameListAdapter = new GameListAdapter(filteredList);
        recyclerView.setAdapter(gameListAdapter);
        gameListAdapter.notifyDataSetChanged();  // data set changed
    }

    @Override
    public void afterTextChanged(Editable editable)
    {

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
        /*
        newQuery = newQuery.toLowerCase();
        //final List<Information> filteredList = new ArrayList<>();
        final List<SearchSuggestion> filteredList = new ArrayList<>();
        //searchView.showProgress();

        for (int j = 0; j < gamelist.size(); j++)
        {

            final String text = gamelist.get(j).title.toLowerCase();
            if (text.contains(newQuery))
            {

                filteredList.add(gamelist.get(j));
                if (filteredList.size() == 20)
                    break;
            }
        }

        searchView.swapSuggestions(filteredList);
        //searchView.hideProgress();


        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // gameListAdapter = new GameListAdapter(filteredList);
        //recyclerView.setAdapter(gameListAdapter);
        // gameListAdapter.notifyDataSetChanged();  // data set c

        */
        newQuery = newQuery.toString().toLowerCase();
        final List<Information> filteredList = new ArrayList<>();

        if (timer != null)
            timer.cancel();
        final String finalNewQuery = newQuery;
        timer = new Timer();
        timer.schedule(
                new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        for (int j = 0; j < gamelist.size(); j++)
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
                        });

                    }
                },
                200
        );


    }


}
