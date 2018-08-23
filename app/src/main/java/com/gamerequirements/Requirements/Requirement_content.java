package com.gamerequirements.Requirements;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gamerequirements.Canyourunit.CanYouRunIt;
import com.gamerequirements.JSONCustom.CustomVolleyRequest;
import com.gamerequirements.MyApplication;
import com.gamerequirements.R;
import com.gamerequirements.SaveCofig.MainActivityConfig;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Requirement_content extends AppCompatActivity implements View.OnClickListener
{
    String requirementurl;
    int id;
    ImageView poster_image;
    TextView[] Headingtextview, ContentTextview, RHeadingtextview, RContentTextview;
    AdView mAdView;
    String summary, genre, date, title;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_content);
        //requirementurl = MyApplication.getURL() + "getreq/";   //for python server
        requirementurl = MyApplication.getURL() + "game/";

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

      findViewById(R.id.Settings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), MainActivityConfig.class));
            }
        });



        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        mAdView = findViewById(R.id.adViewContentBottom);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("68E07D1A7FD2CC0EB313746EF7621A6C")
                // .addTestDevice("68E07D1A7FD2CC0EB313746EF7621A6C")
                .build();
        mAdView.loadAd(adRequest);
        poster_image = findViewById(R.id.image);
        Picasso.with(this)
                .load(MyApplication.getImageurl() + id)
                .into(poster_image);
        poster_image.setOnClickListener(this);
        Headingtextview = new TextView[]{
                findViewById(R.id.HEADING_intelCPU),
                findViewById(R.id.HEADING_AMD_CPU),
                findViewById(R.id.HEADING_NvidiaGPU),
                findViewById(R.id.HEADING_AMDGpu),
                findViewById(R.id.HEADING_VRAM),
                findViewById(R.id.HEADING_RAM),
                findViewById(R.id.HEADING_OS),
                findViewById(R.id.HEADING_HDD),

        };
        ContentTextview = new TextView[]{
                findViewById(R.id.CONTENT_intelCPU),
                findViewById(R.id.CONTENT_AMD_CPU),
                findViewById(R.id.CONTENT_NvidiaGPU),
                findViewById(R.id.CONTENT_AMDGpu),
                findViewById(R.id.CONTENT_VRAM),
                findViewById(R.id.CONTENT_RAM),
                findViewById(R.id.CONTENT_OS),
                findViewById(R.id.CONTENT_HDD),

        };
        RHeadingtextview = new TextView[]{
                findViewById(R.id.RHEADING_intelCPU),
                findViewById(R.id.RHEADING_AMD_CPU),
                findViewById(R.id.RHEADING_NvidiaGPU),
                findViewById(R.id.RHEADING_AMDGpu),
                findViewById(R.id.RHEADING_VRAM),
                findViewById(R.id.RHEADING_RAM),
                findViewById(R.id.RHEADING_OS),
                findViewById(R.id.RHEADING_HDD),

        };
        RContentTextview = new TextView[]{
                findViewById(R.id.RCONTENT_intelCPU),
                findViewById(R.id.RCONTENT_AMD_CPU),
                findViewById(R.id.RCONTENT_NvidiaGPU),
                findViewById(R.id.RCONTENT_AMDGpu),
                findViewById(R.id.RCONTENT_VRAM),
                findViewById(R.id.RCONTENT_RAM),
                findViewById(R.id.RCONTENT_OS),
                findViewById(R.id.RCONTENT_HDD),

        };
        //MYURL="http://www.game-debate.com/games/index.php?g_id=" + id;
        //Toast.makeText(this, id + "", Toast.LENGTH_LONG).show();
        displaydata(id);
    }

    void displaydata(int id)
    {
        String url = requirementurl + id;
        JsonObjectRequest jsonarrayrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
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
                Log.e("Error", error.toString());
            }
        });
        RequestQueue requestqueue = CustomVolleyRequest.getInstance(this.getApplicationContext()).getRequestQueue();
        requestqueue.add(jsonarrayrequest);
    }


    void handleresponse(JSONObject response)
    {
        try
        {
            Log.d("TAG", response.toString());
            TextView date_TV = findViewById(R.id.date);
            title = response.getString("title");
            genre = response.getString("genre");
            summary = response.getString("summary");
            date = response.getString("release_date");


            if (date.equals(""))
                date_TV.setVisibility(View.GONE);
            else
                date_TV.setText("Release Date: " + date);
            ((TextView) findViewById(R.id.game_title)).setText(title);

            try
            {
                ((TextView) findViewById(R.id.genre)).setText("Genre: " + genre.substring(2, genre.length() - 2).replace("\\n", ", "));
            } catch (StringIndexOutOfBoundsException e)
            {
                Log.d("Error", e.toString() + "gid = " + id);
            }

            ((TextView) findViewById(R.id.summary)).setText(summary);
            try
            {
                JSONObject min_jobj = response.getJSONObject("minimum_req");
                load_requirements(min_jobj, "minimum");
            } catch (JSONException e)
            {
                findViewById(R.id.MinimumLL).setVisibility(View.GONE);
                e.printStackTrace();
            }
            JSONObject rec_jobj = response.getJSONObject("recommended_req");

            Log.d("TAG", rec_jobj.toString());
            load_requirements(rec_jobj, "recommended");

        } catch (JSONException e)
        {
            e.printStackTrace();

        }
        finally
        {
            findViewById(R.id.progress_view).setVisibility(View.GONE);
            findViewById(R.id.container).setVisibility(View.VISIBLE);
        }
    }

    /**
     * throws cause the calling method to handle exceptio instead of handling the exception here
     **/
    void load_requirements(JSONObject jarr, String type) throws JSONException
    {
        String Intel_CPU = jarr.getString("intel_cpu");
        String AMD_CPU = jarr.getString("amd_cpu");
        String NvidiaGPU = jarr.getString("nvidia_gpu");
        String AMDGpu = jarr.getString("amd_gpu");
        String RAM = jarr.getString("ram");
        String OS = jarr.getString("os");
        String HDD = jarr.getString("hdd");
        String vram = jarr.getString("vram");
        String[] ContentArray = {Intel_CPU, AMD_CPU, NvidiaGPU, AMDGpu, vram, RAM, OS, HDD};
        String[] HeadingArray = {"Intel CPU", "AMD CPU", "Nvidia Graphics Card", "AMD Graphics Card", "VRAM", "RAM", "Operating System", "Hard Drive Space"};
        Log.d("array", String.valueOf(jarr.length()));
        if (type.equals("minimum"))
            for (int i = 0; i < jarr.length() - 1; i++)
            {
                Headingtextview[i].setText(HeadingArray[i]);
                ContentTextview[i].setText(ContentArray[i]);
            }
        else
            for (int i = 0; i < jarr.length() - 1; i++)
            {
                RHeadingtextview[i].setText(HeadingArray[i]);
                RContentTextview[i].setText(ContentArray[i]);
            }
    }

   /* void load_recommended(JSONObject jarr) throws JSONException
    {
        String Intel_CPU = jarr.getString("intel_cpu");
        String AMD_CPU = jarr.getString("amd_cpu");
        String NvidiaGPU = jarr.getString("nvidia_gpu");
        String AMDGpu = jarr.getString("amd_gpu");
        String RAM = jarr.getString("ram");
        String OS = jarr.getString( "os");
        String HDD = jarr.getString("hdd");
        String vram = jarr.getString("vram");

        String[] ContentArray = {Intel_CPU, AMD_CPU, NvidiaGPU, AMDGpu, RAM, OS, HDD};
        String[] HeadingArray = {"Intel CPU", "AMD CPU", "Nvidia Graphics Card", "AMD Graphics Card", "RAM", "Operating System", "Hard Drive Space"};
        for (int i = 0; i < recommended_array.length(); i++)
        {
            RHeadingtextview[i].setText(HeadingArray[i]);
            RContentTextview[i].setText(ContentArray[i]);
        }
    } */

    public void canYouRunIt(View view)
    {
        Intent i = new Intent(this, CanYouRunIt.class).putExtra("gid", id);
        startActivity(i);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.image:
                Dialog settingsDialog = new Dialog(this);
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                        , null));
                ImageView imageView = settingsDialog.findViewById(R.id.imageview1);
                settingsDialog.show();
                Log.d("ImageLOading",MyApplication.getImageurl() + id);
                Picasso.with(this)
                        .load(MyApplication.getImageurl() + id)
                        .into(imageView);

                Picasso.Builder builder = new Picasso.Builder(this);
                builder.listener(new Picasso.Listener()
                {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                    {
                        Log.d("ImageLOading",exception.getMessage());
                    }
                });
                builder.build().load(MyApplication.getImageurl() + id).into(imageView);


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
