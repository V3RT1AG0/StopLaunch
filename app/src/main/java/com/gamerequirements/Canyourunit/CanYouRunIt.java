package com.gamerequirements.Canyourunit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.gamerequirements.ActivitySuperClass;
import com.gamerequirements.R;
import com.gamerequirements.Singelton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

public class CanYouRunIt extends ActivitySuperClass
{
    int gid;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_you_run_it);
        gid = getIntent().getIntExtra("gid", 0);
        SharedPreferences sharedPreferences = this.getSharedPreferences(Singelton.getSharedPrefrenceKey(), Context.MODE_PRIVATE);

        Boolean bool = sharedPreferences.getBoolean("StoredConfigEnabled", false);
        Log.d("Hello", bool.toString());
        if (sharedPreferences.getBoolean("StoredConfigEnabled", false))
        {
            String CPUid = sharedPreferences.getString("CPUkey", null),
                    GPUid = sharedPreferences.getString("GPUkey", null),
                    RAMid = sharedPreferences.getString("RAMkey", null);

            url = Singelton.getURL() + "request2.php?g_id=" + gid + "&p_id=" + CPUid + "&ram=" + RAMid + "&gc_id=" + GPUid;
            //https://f8ct.com/gr/request.php?g_id=8514&p_id=2391&ram=3&gc_id=517
        } else
        {
            url = Singelton.getURL() + "request2.php?g_id=" + gid;
            //https://f8ct.com/gr/request.php?g_id=8514
        }


        NativeExpressAdView adView2 = (NativeExpressAdView) findViewById(R.id.adViewBottom);
        AdRequest request = new AdRequest.Builder()
                .build();
        adView2.loadAd(request);

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

        Log.d("Hello", url);
        webView.loadUrl(url);
    }
}
