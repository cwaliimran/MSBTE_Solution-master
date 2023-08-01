package com.msbte.modelanswerpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;



import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;

import java.io.IOException;

public class DumpsPDFActivity extends AppCompatActivity {

    String websiteURL;
    String strNew3;
    int rewardAmount = 0;

    private WebView webview;
    private ProgressBar progressBar;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dumps_pdfactivity);

        // To prevent an Android application from taking screenshots and recording the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // prevent the keyboard from appearing
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        progressBar = findViewById(R.id.progress_bar5);
        webview = findViewById(R.id.webView5);
        progressBar.setMax(100);
        progressBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        netcheck();

        websiteURL = getIntent().getStringExtra("url");
        Intent intent = getIntent();

        if (!CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            //if there is no internet do this
            setContentView(R.layout.activity_dumps_pdfactivity);
            //Toast.makeText(this,"No Internet Connection, Chris",Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(this) //alert the person knowing they are about to close
                    .setTitle("No internet connection available")
                    .setMessage("Please Check you're Mobile data or Wifi network.")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    //.setNegativeButton("No", null)
                    .show();

        } else {
            //Webview stuff


            webview.setWebViewClient(new WebViewClientDemo());
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setLoadsImagesAutomatically(true);
            webview.getSettings().setBuiltInZoomControls(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
            webview.loadUrl(websiteURL);

            webview.loadUrl(websiteURL);
            progressBar.setProgress(0);
            netcheck();

            webview.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    progressBar.setProgress(newProgress);
                    if (newProgress == 100)
                        progressBar.setVisibility(View.GONE);
                    else
                        progressBar.setVisibility(View.VISIBLE);
                    netcheck();
                    super.onProgressChanged(view, newProgress);
                }
            });
            netcheck();
        }

        try {
            webview.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                    netcheck();

//                    String webUrl = webview.getUrl();
//
//                    String substring = "file/d";
//                    boolean reuslt = webUrl.contains(substring);
//
//                    String substring2 = "econtent.msbte";
//                    boolean reuslt2 = webUrl.contains(substring2);


                    new AlertDialog.Builder(DumpsPDFActivity.this) //alert the person knowing they are about to close
                            .setTitle("Attention all users \uD83D\uDEAB")
                            .setMessage("The downloading feature was closed. We apologize for any inconvenience caused.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressBar.setProgress(0);
                                    progressBar.setVisibility(View.GONE);

                                }
                            })
                            .show();
                }
            });


        } catch (Exception e) {
            Toast.makeText(DumpsPDFActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            Toast.makeText(DumpsPDFActivity.this, "Please Check Your Internet Connection...", Toast.LENGTH_LONG).show();
        }
        netcheck();

    }


    private class WebViewClientDemo extends WebViewClient {
        @Override
        //Keep webview in app when clicking links
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private void netcheck() {

        if (!CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            webview = findViewById(R.id.webView);
            webview.loadUrl("file:///android_asset/error.html");
        }

    }
}