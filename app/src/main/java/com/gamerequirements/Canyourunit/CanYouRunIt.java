package com.gamerequirements.Canyourunit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gamerequirements.ActivitySuperClass;
import com.gamerequirements.Default;
import com.gamerequirements.JSONCustom.CustomVolleyRequest;
import com.gamerequirements.R;
import com.gamerequirements.Singelton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

import org.apache.http.util.EncodingUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CanYouRunIt extends ActivitySuperClass
{
    int gid;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_you_run_it);
        //String postData = null;
        gid = getIntent().getIntExtra("gid", 0);
        SharedPreferences sharedPreferences = this.getSharedPreferences(Singelton.getSharedPrefrenceKey(), Context.MODE_PRIVATE);

        Boolean bool = sharedPreferences.getBoolean("StoredConfigEnabled", false);
        Log.d("Hello", bool.toString());
        if (sharedPreferences.getBoolean("StoredConfigEnabled", false))
        {
            String CPUid = sharedPreferences.getString("CPUkey", null),
                    GPUid = sharedPreferences.getString("GPUkey", null),
                    RAMid = sharedPreferences.getString("RAMkey", null);

               /* postData = "p_id=" + URLEncoder.encode((CPUid), "UTF-8") +
                        "gc_id=" + URLEncoder.encode((GPUid), "UTF-8") +
                        "ram=" + URLEncoder.encode((RAMid), "UTF-8") +
                        "g_id=" + URLEncoder.encode(Integer.toString(gid), "UTF-8");*/

            url = Singelton.getURL() + "request.php?g_id=" + gid + "&p_id=" + CPUid + "&ram=" + RAMid + "&gc_id=" + GPUid;
            //https://f8ct.com/gr/request.php?g_id=8514&p_id=2391&ram=3&gc_id=517
        } else
        {
               /* postData = "gid=" + URLEncoder.encode(Integer.toString(gid), "UTF-8"); */
            url = Singelton.getURL() + "request.php?g_id=" + gid;
            //https://f8ct.com/gr/request.php?g_id=8514
        }


        NativeExpressAdView adView2 = (NativeExpressAdView) findViewById(R.id.adViewBottom);
        AdRequest request = new AdRequest.Builder()
                .build();
        adView2.loadAd(request);


        // String url = "http://flipaccounts.com/game.html";

        final CircularProgressView circularProgressView = (CircularProgressView) findViewById(R.id.progress_circle);
        final WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int progress)
            {
                if (progress == 100)
                {
                    circularProgressView.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);

                } else
                {
                    circularProgressView.setVisibility(View.VISIBLE);

                }
            }
        });


        //webView.postUrl(Singelton.getURL() + "request.php", EncodingUtils.getBytes(postData, "BASE64"));
        // String url = Singelton.getURL() + "request.php?g_id="+ gid;
        Log.d("Hello", url);
        webView.loadUrl(url);
    }
}
