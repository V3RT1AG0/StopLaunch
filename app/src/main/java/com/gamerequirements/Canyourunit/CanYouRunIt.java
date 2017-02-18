package com.gamerequirements.Canyourunit;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_you_run_it);
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id1));
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id2));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdView adView2 = (AdView)findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice("92E6517DC022A908D6E9828A8C2EE0CB").
                        addTestDevice("9CF2934004AD49BD731B33A065A2621E").build();

        mAdView.loadAd(adRequest);
        adView2.loadAd(adRequest);
        String url="http://flipaccounts.com/game.html";
        gid=getIntent().getIntExtra("gid",0);
        final CircularProgressView circularProgressView= (CircularProgressView)findViewById(R.id.progress_circle);
        final WebView webView= (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    circularProgressView.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);

                }else {
                    circularProgressView.setVisibility(View.VISIBLE);

                }
            }
        });
        String postData = null;
        try
        {
            postData = "gid="+ URLEncoder.encode(Integer.toString(gid), "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }


        webView.postUrl(Singelton.getURL()+"request.php", EncodingUtils.getBytes(postData, "BASE64"));
        //webView.loadUrl(url);
    }
}
