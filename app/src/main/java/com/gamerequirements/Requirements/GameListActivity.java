package com.gamerequirements.Requirements;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.gamerequirements.EndlessRecyclerView;
import com.gamerequirements.JSONCustom.CustomRequest;
import com.gamerequirements.JSONCustom.CustomVolleyRequest;
import com.gamerequirements.MyApplication;
import com.gamerequirements.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameListActivity extends Fragment implements FloatingSearchView.OnQueryChangeListener
{
    /**
     * code for python server
     **/
    //private static final String gamelisturl = MyApplication.getURL() + "loadlist";
    private static final String COUNT = "count";
    //private static final String gamelisturl = MyApplication.getURL() + "index.php";
    String gamelisturl, notificationCountUrl, SEARCHURL, GENREURL;
    List<Information> gamelist;
    GameListAdapter gameListAdapter;
    GameListAdapter searchAdapter;
    RecyclerView recyclerView, filteredRecyclerView;
    FloatingSearchView searchView;
    boolean doubleBackToExitPressedOnce = false;
    CircularProgressView progressView;
    TextView notificationCounttextview;
    Timer timer;
    LinearLayout errorlayout, genreLL;
    SharedPreferences sharedPrefs;
    int curSize;
    Boolean nexttexnupdate = false;
    //JSONObject idListjsonObject=new JSONObject();
    ArrayList<String> idArrayList = new ArrayList<>();
    final List<Information> filteredList = new ArrayList<>();
    LinearLayoutManager lmanager;
    EndlessRecyclerView endlessRecyclerView;
    Button selectedbutton;

    public static GameListActivity newInstance()
    {
        GameListActivity fragment = new GameListActivity();
        return fragment;
    }

    public GameListActivity()
    {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {

        super.onActivityCreated(savedInstanceState);
        sharedPrefs = MyApplication.getContext().getSharedPreferences("com.gamerequirements", Context.MODE_PRIVATE);

        gamelisturl = MyApplication.getURL() + "gameslist";
        notificationCountUrl = MyApplication.getURL() + "newgamescount";
        SEARCHURL = MyApplication.getURL() + "gameslist/search/";
        GENREURL = MyApplication.getURL() + "gameslistgenre/";


        genreLL = getActivity().findViewById(R.id.genre_LL);
        timer = new Timer();
        progressView = getActivity().findViewById(R.id.progress_view);
        progressView.startAnimation();
        gamelist = new ArrayList<>();
        recyclerView = getActivity().findViewById(R.id.my_gamelist_recycler);
        filteredRecyclerView = getActivity().findViewById(R.id.my_filtered_recycler);
        //notificationCounttextview = getActivity().findViewById(R.id.badge);
        lmanager = new LinearLayoutManager(getActivity());
        errorlayout = getActivity().findViewById(R.id.errorlayout);


        gameListAdapter = new GameListAdapter(gamelist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lmanager);
        recyclerView.setAdapter(gameListAdapter);


        searchAdapter = new GameListAdapter(filteredList);
        filteredRecyclerView.setHasFixedSize(true);
        filteredRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        filteredRecyclerView.setAdapter(searchAdapter);


        searchView = getActivity().findViewById(R.id.floating_search_view);
        searchView.setOnQueryChangeListener(this);
        searchView.setSearchFocused(true);

        AddGenresToLayoutDynamically();
        AddOnScrollListenrerToRecyclerView();
        VolleyOperation();
       // getNotificationCount();

        Log.e("Error", gamelisturl);

    }

    private void AddGenresToLayoutDynamically()
    {
        String[] genres = {"Action", "Adventure", "Arcade", "Casual", "Fighting", "Management", "Tabletop", "MOBA", "Online", "Platformer", "Puzzler", "Racing", "RPG", "Sandbox", "Shooter", "Sim", "Stealth", "Sport", "Strategy"};
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        for (String genre : genres)
        {
            Button button = (Button) inflater.inflate(R.layout.genre_capsule_button, null);
            button.setText(genre);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    100
            );
            params.setMargins(20,0,0,0);
            button.setLayoutParams(params);
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    genreButtonPressed((Button) view);
                }
            });
            genreLL.addView(button);
        }
    }

    void genreButtonPressed(Button button)
    {
        if (selectedbutton != null)
        {
            if (button == selectedbutton)
            {
                selectedbutton = null;
            } else
            {
                selectedbutton = button;
            }
        } else
        {
            selectedbutton = button;
        }
// String url = "http://192.168.31.59:5000/gameslistgenre/"+genre;
        if (selectedbutton != null)
        {
            String genre = selectedbutton.getText().toString();
            VolleySearchRequest(GENREURL + genre);
        } else
            VolleyOperation();
    }

    private void AddOnScrollListenrerToRecyclerView()
    {
        Log.d("Hello", "hello" + gamelist);
        endlessRecyclerView = new EndlessRecyclerView(lmanager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                Log.d("Tag1", page + " " + totalItemsCount + " ");
                curSize = gameListAdapter.getItemCount();
                nexttexnupdate = true;
                VolleyOperation();
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerView);
    }

    private void getNotificationCount()
    {

        CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.GET, notificationCountUrl, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    int count = response.getInt("count");
                    int storedCount = sharedPrefs.getInt("count", 0);
                    if (storedCount == count)
                        return;
                    notificationCounttextview.setText(Integer.toString(count - storedCount));
                    notificationCounttextview.setVisibility(View.VISIBLE);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("Error", error.toString());
            }
        });

        RequestQueue requestqueue = CustomVolleyRequest.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        requestqueue.add(jsonObjectRequest);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_game_list, container, false);
        return (v);
    }


    private void VolleyOperation()
    {

        HashMap<String, String> params = new HashMap<>();
        JSONArray jsonArray = new JSONArray(idArrayList);

        params.put("list", jsonArray.toString());
        Log.d("array", params.toString());
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
                resetRecyclerViewData();
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
                    TextView textView = errorlayout.findViewById(R.id.errorMessage);
                    textView.setText("Check your connection and try again");
                    //  Toast.makeText(GameListActivity.this, "Please check your connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestqueue = CustomVolleyRequest.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        requestqueue.add(jsonObjectRequest);
    }

    private void resetRecyclerViewData()
    {
        gamelist.clear();
        gameListAdapter.notifyDataSetChanged();
        endlessRecyclerView.resetState();
        nexttexnupdate = false;
        idArrayList.clear();


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
                filteredRecyclerView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                gameListAdapter.notifyDataSetChanged();
            }
            /*SharedPreferences.Editor editor = sharedPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(gamelist);
            editor.putString("database", json);
            editor.putInt("dbv",MyApplication.getDatabaseversion());
            editor.commit();*/
        }
    }


  /*  @Override
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
    }*/

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery)
    {
        if (newQuery.length() == 0)
        {
            timer.cancel();
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
                               String url = SEARCHURL + finalNewQuery;
                               VolleySearchRequest(url);
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
                300
        );


    }


    void VolleySearchRequest(final String url)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                recyclerView.setVisibility(View.GONE);
                progressView.setVisibility(View.VISIBLE);
            }
        });


        RequestQueue requestqueue = CustomVolleyRequest.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        requestqueue.cancelAll("search");
        CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
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
                resetRecyclerViewData();
                errorlayout.findViewById(R.id.RetryButton).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Log.d("custom", "volley2");
                        errorlayout.setVisibility(View.GONE);
                        progressView.setVisibility(View.VISIBLE);
                        searchView.clearQuery();
                        VolleyOperation();
                    }
                });
                Log.d("Error", error.toString());
                if (error instanceof NoConnectionError)
                {
                    TextView textView = errorlayout.findViewById(R.id.errorMessage);
                    textView.setText("Check your connection and try again");
                    //Toast.makeText(GameListActivity.this, "Please check your connection and try again", Toast.LENGTH_LONG).show();
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
            searchAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.GONE);
            filteredRecyclerView.setVisibility(View.VISIBLE);

        }

    }
}
