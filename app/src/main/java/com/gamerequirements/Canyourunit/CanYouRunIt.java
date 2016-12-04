package com.gamerequirements.Canyourunit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.gamerequirements.ActivitySuperClass;
import com.gamerequirements.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class CanYouRunIt extends ActivitySuperClass
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_you_run_it);
        String url="http://thedevoteenetwork.com/game.html";
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
        webView.loadUrl(url);
    }
}
