package com.gamerequirements.Requirements;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.gamerequirements.ActivitySuperClass;
import com.gamerequirements.JSONCustom.CustomVolleyRequest;
import com.gamerequirements.R;
import com.gamerequirements.Singelton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class Requirement_content extends ActivitySuperClass
{
    private static final String requirementurl = Singelton.getURL() + "getreq/";
    int id;
    String title;
    ImageView poster_image;
    TextView[] Headingtextview, ContentTextview, RHeadingtextview, RContentTextview;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_content);
        Bundle bundle=getIntent().getExtras();
        id = bundle.getInt("id");
        title = bundle.getString("title");
        String genre=bundle.getString("genre");
        String date=bundle.getString("date");
        String summary=bundle.getString("summary");
        ((TextView) findViewById(R.id.game_title)).setText(title);
        ((TextView) findViewById(R.id.genre)).setText("Genre: "+genre);
        ((TextView) findViewById(R.id.date)).setText("Release Date: "+date);
        ((TextView) findViewById(R.id.summary)).setText(summary);

        poster_image = (ImageView) findViewById(R.id.image);
        Picasso.with(this)
                .load(Singelton.getImageurl() + id)
                .into(poster_image);


        Headingtextview = new TextView[]{
                ((TextView) findViewById(R.id.HEADING_intelCPU)),
                ((TextView) findViewById(R.id.HEADING_AMD_CPU)),
                ((TextView) findViewById(R.id.HEADING_NvidiaGPU)),
                ((TextView) findViewById(R.id.HEADING_AMDGpu)),
                ((TextView) findViewById(R.id.HEADING_RAM)),
                ((TextView) findViewById(R.id.HEADING_OS)),
                ((TextView) findViewById(R.id.HEADING_HDD)),

        };

        ContentTextview = new TextView[]{
                ((TextView) findViewById(R.id.CONTENT_intelCPU)),
                ((TextView) findViewById(R.id.CONTENT_AMD_CPU)),
                ((TextView) findViewById(R.id.CONTENT_NvidiaGPU)),
                ((TextView) findViewById(R.id.CONTENT_AMDGpu)),
                ((TextView) findViewById(R.id.CONTENT_RAM)),
                ((TextView) findViewById(R.id.CONTENT_OS)),
                ((TextView) findViewById(R.id.CONTENT_HDD)),

        };

        RHeadingtextview = new TextView[]{
                ((TextView) findViewById(R.id.RHEADING_intelCPU)),
                ((TextView) findViewById(R.id.RHEADING_AMD_CPU)),
                ((TextView) findViewById(R.id.RHEADING_NvidiaGPU)),
                ((TextView) findViewById(R.id.RHEADING_AMDGpu)),
                ((TextView) findViewById(R.id.RHEADING_RAM)),
                ((TextView) findViewById(R.id.RHEADING_OS)),
                ((TextView) findViewById(R.id.RHEADING_HDD)),

        };

        RContentTextview = new TextView[]{
                ((TextView) findViewById(R.id.RCONTENT_intelCPU)),
                ((TextView) findViewById(R.id.RCONTENT_AMD_CPU)),
                ((TextView) findViewById(R.id.RCONTENT_NvidiaGPU)),
                ((TextView) findViewById(R.id.RCONTENT_AMDGpu)),
                ((TextView) findViewById(R.id.RCONTENT_RAM)),
                ((TextView) findViewById(R.id.RCONTENT_OS)),
                ((TextView) findViewById(R.id.RCONTENT_HDD)),

        };

        //MYURL="http://www.game-debate.com/games/index.php?g_id=" + id;
        Toast.makeText(this, id + "", Toast.LENGTH_LONG).show();
        displaydata(id);
    }

    void displaydata(int id)
    {
        String url = requirementurl + id;
        JsonArrayRequest jsonarrayrequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
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
            JSONArray jarr = response.getJSONArray(0);
            try
            {
                load_minimum(jarr);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            JSONArray recommended_array = response.getJSONArray(1);
            if (recommended_array.length() != 1)
            {
                load_recommended(recommended_array);
            }
            else
            {
                (findViewById(R.id.RecommendedLL)).setVisibility(View.GONE);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();

        }
    }

    /**
     * throws cause the calling method to handle exceptio instead of handling the exception here
     **/
    void load_minimum(JSONArray jarr) throws JSONException
    {
        String Intel_CPU = jarr.getString(0);
        String AMD_CPU = jarr.getString(1);
        String NvidiaGPU = jarr.getString(2);
        String AMDGpu = jarr.getString(3);
        String RAM = jarr.getString(4);
        String OS = jarr.getString(5);
        String HDD = "";
        if (jarr.length() == 7)
        {
            HDD = jarr.getString(6);
        }
        else
        {
            Headingtextview[6].setVisibility(View.GONE);
            ContentTextview[6].setVisibility(View.GONE);
        }
        String[] ContentArray = {Intel_CPU, AMD_CPU, NvidiaGPU, AMDGpu, RAM, OS, HDD};
        String[] HeadingArray = {"Intel CPU", "AMD CPU", "Nvidia Graphics Card", "AMD Graphics Card", "RAM", "Operating System", "Hard Drive Space"};
        for (int i = 0; i < jarr.length(); i++)
        {
            Headingtextview[i].setText(HeadingArray[i]);
            ContentTextview[i].setText(ContentArray[i]);
        }

        Log.d("TAG", HDD);
    }

    void load_recommended(JSONArray recommended_array) throws JSONException
    {
        String Intel_CPU2 = recommended_array.getString(0);
        String AMD_CPU2 = recommended_array.getString(1);
        String NvidiaGPU2 = recommended_array.getString(2);
        String AMDGpu2 = recommended_array.getString(3);
        String RAM2 = recommended_array.getString(4);
        String OS2 = recommended_array.getString(5);
        String HDD2="";
        if (recommended_array.length() == 7)
        {
            HDD2 = recommended_array.getString(6);
        }
        else
        {
            RHeadingtextview[6].setVisibility(View.GONE);
            RContentTextview[6].setVisibility(View.GONE);
        }
        String[] ContentArray = {Intel_CPU2, AMD_CPU2, NvidiaGPU2, AMDGpu2, RAM2, OS2, HDD2};
        String[] HeadingArray = {"Intel CPU", "AMD CPU", "Nvidia Graphics Card", "AMD Graphics Card", "RAM", "Operating System", "Hard Drive Space"};
        for (int i = 0; i < recommended_array.length(); i++)
        {
            RHeadingtextview[i].setText(HeadingArray[i]);
            RContentTextview[i].setText(ContentArray[i]);
        }
    }
   /* void performdatascrapping(final String url)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Document document = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                    final Elements e = document.select(div[class="systemRequirementsWrapBox gameSystemRequirementsWrapBox"]);
                    //Elements firstfoura=e.select("a");
                    //String str=firstfoura.first().text();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            Toast.makeText(Requirement_content.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    });

                    Log.d("PEDA", e.toString());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    } */
}
