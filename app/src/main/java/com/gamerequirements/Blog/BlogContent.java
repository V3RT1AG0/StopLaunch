package com.gamerequirements.Blog;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
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
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONException;
import org.json.JSONObject;


public class BlogContent extends ActivitySuperClass
{


    String blogUrl = MyApplication.getBlogUrl()+ "wp-json/wp/v2/posts/";
    int blogpostid;
    WebView webView;
    TextView blogtitetView;
    Toolbar myToolbar;
    CircularProgressView circularProgressView;
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
        circularProgressView = findViewById(R.id.progress_view);
        circularProgressView.startAnimation();
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
            //webView.setWebChromeClient(new WebChromeClient());
            //webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new Browser());
            webView.setWebChromeClient(new MyWebClient());
            webView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            webView.loadData( "<style>img{display: inline;height: auto;width: 100%;}p{color:#FFFFFF;}</style>"+content, "text/html", "UTF-8");
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

    class Browser
            extends WebViewClient
    {
        Browser() {}

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
        {
            paramWebView.loadUrl(paramString);
            return true;
        }
    }

    public class MyWebClient
            extends WebChromeClient
    {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public MyWebClient() {}

        @Override
        public void onProgressChanged(WebView view, int progress)
        {
            super.onProgressChanged(view, progress);
            if (progress == 100)
            {
                circularProgressView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            } else
            {
                circularProgressView.setVisibility(View.VISIBLE);
            }
        }



        public Bitmap getDefaultVideoPoster()
        {
            if (BlogContent.this == null) {
                return null;
            }
            return BitmapFactory.decodeResource(BlogContent.this.getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)BlogContent.this.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            BlogContent.this.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            BlogContent.this.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = BlogContent.this.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = BlogContent.this.getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
             ((FrameLayout)BlogContent.this.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            BlogContent.this.getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }
}
