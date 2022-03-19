package com.sityoy.eduwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    WebView webView;
    ProgressBar bar;
    private SwipeRefreshLayout refreshLayout;
    EditText etSource,etDestination;
    Button btTrack;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        bar = (ProgressBar) findViewById(R.id.progressBar1);
        webView.setWebViewClient(new WebViewClient());

        //Assign variable

        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://eduwork.id");
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setDomStorageEnabled(true);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(this);

        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }
        });
    }

    public void open(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0.7893,113.9213"));
        startActivity(intent);
    }



    @Override
    public void onRefresh() {
        webView.reload();
        refreshLayout.setRefreshing(false);
    }
    public abstract class myWebclient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            bar.setVisibility(View.GONE);
        }


        String currentUrl;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            currentUrl = url;

            if (url.startsWith("http") || url.startsWith("https")) {
                return false;
            }
            if (url.startsWith("intent")) {

                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                    String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                    if (fallbackUrl != null) {
                        webView.loadUrl(fallbackUrl);
                        return true;
                    }
                } catch (URISyntaxException e) {
                    //not an intent uri
                }
                return true;//do nothing in other cases
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public abstract boolean shouldOverriderUrlLoading(WebView view, String url);
    }

    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Yakin loe mau keluar? Periksa Datanya..!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


}