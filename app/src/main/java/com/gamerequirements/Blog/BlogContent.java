package com.gamerequirements.Blog;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gamerequirements.ActivitySuperClass;
import com.gamerequirements.JSONCustom.CustomRequest;
import com.gamerequirements.JSONCustom.CustomVolleyRequest;
import com.gamerequirements.MyApplication;
import com.gamerequirements.R;

import org.json.JSONException;
import org.json.JSONObject;


public class BlogContent extends ActivitySuperClass
{


    String blogUrl = MyApplication.getBlogUrl()+ "wp-json/wp/v2/posts/";
    int blogpostid;
    WebView webView;
    TextView blogtitetView;
    Toolbar myToolbar;
   // CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_content);
        //progressView = findViewById(R.id.progress_view);
        blogtitetView = findViewById(R.id.blogTitleTView);
        //progressView.startAnimation();
        webView =  findViewById(R.id.webview1);
        myToolbar =  findViewById(R.id.my_toolbar);
        blogpostid = getIntent().getIntExtra("id",0);
        setSupportActionBar(myToolbar);
        Jsonrequest();


    }

    void Jsonrequest()
    {
        RequestQueue requestQueue = CustomVolleyRequest.getInstance(this.getApplicationContext()).getRequestQueue();
        CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.GET, blogUrl+blogpostid,null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.d("TAG1", "xxxxxx");
                processresponse(response);
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    void processresponse(JSONObject jsonObject)
    {
        Log.d("TAG1", jsonObject.toString());
        if (jsonObject == null)//uncomment later
        {
            Log.d("TAG1", "error");
            return;
        }
        try
        {


            String title = jsonObject.getJSONObject("title").getString("rendered");
            String  content = jsonObject.getJSONObject("content").getString("rendered");
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData( "<style>img{display: inline;height: auto;width: 100%;}</style>"+content, "text/html", "UTF-8");
            /*webView.setWebViewClient(new WebViewClient()
            {
                public void onPageFinished(WebView view, String url)
                {
                    progressView.setVisibility(View.GONE);
                }
            });*/
            blogtitetView.setText(title);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }


    }
}
