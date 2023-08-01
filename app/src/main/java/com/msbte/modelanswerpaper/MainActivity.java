package com.msbte.modelanswerpaper;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.io.File;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.TrustManagerFactory;


public class MainActivity extends AppCompatActivity {

    String websiteURL;
    String strNew3;
    int rewardAmount = 0;

    private WebView webview;
    private ProgressBar progressBar;
    private long backPressedTime;
    private Toast backToast;

    private AdView mAdView;
    private RewardedAd mRewardedAd;

    CoordinatorLayout layout;

    ReviewManager manager;
    ReviewInfo reviewInfo;

    @SuppressLint("WebViewClientOnReceivedSslError")
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//        IronSource.init(MainActivity.this, "1201f8ea5", IronSource.AD_UNIT.REWARDED_VIDEO);


        mAdView = findViewById(R.id.adView2_main);
        AdRequest adRequest = new AdRequest.Builder().build();
        if (mAdView != null) {
            mAdView.loadAd(adRequest);
        }

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
//                mAdView.loadAd(adRequest);
            }

        });

//        setAds();

        websiteURL = getIntent().getStringExtra("url");
        Intent intent = getIntent();

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setMax(100);
        progressBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        netcheck();

        if (!CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            //if there is no internet do this
            setContentView(R.layout.activity_main);
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
            webview = findViewById(R.id.webView);
            webview.setWebViewClient(new WebViewClientDemo());
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setLoadsImagesAutomatically(true);
            webview.getSettings().setBuiltInZoomControls(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.getSettings().setAllowFileAccessFromFileURLs(true);
            webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
            webview.clearSslPreferences();
            webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

            //Those other methods I tried out of despair just in case
            webview.clearFormData();
            webview.clearCache(true);
            webview.clearHistory();
            webview.clearMatches();


            // Add SSL related configurations
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE); // Allow loading mixed content
            webview.getSettings().setBlockNetworkLoads(false); // Allow network loads


//           webview.setWebViewClient(new Myclient2());

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

        //Runtime External storage permission for saving download files
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            }
        }

        netcheck();

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


                    new AlertDialog.Builder(MainActivity.this) //alert the person knowing they are about to close
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
            Toast.makeText(MainActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this, "Please Check Your Internet Connection...", Toast.LENGTH_LONG).show();
        }
        netcheck();
    }

    private boolean downloadTask(String url) throws Exception {
        if (!url.startsWith("http")) {
            return false;
        }
        String name = "MSBTE_Solution_App.pdf";
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Download");
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.mkdirs();
            }
            File result = new File(file.getAbsolutePath() + File.separator + name);
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setDestinationUri(Uri.fromFile(result));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
            }
            //mToast(mContext, "Starting download...");
            MediaScannerConnection.scanFile(MainActivity.this, new String[]{result.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } catch (Exception e) {
            Log.e(">>>>>", e.toString());
            return false;
        }
        return true;
    }

    //load ads
    private void setAds() {

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, getString(R.string.reward_id),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.

                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;

                    }
                });

    }

    // show ads
    private void loadreward() {

        if (mRewardedAd != null) {
            mRewardedAd.show(MainActivity.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    String checkad = String.valueOf(rewardItem.getAmount());
                    String rewardType = rewardItem.getType();

                    // gift of users
//                    String rewardName = placement.getRewardName();
//                    rewardAmount = placement.getRewardAmount();
                    Toast.makeText(MainActivity.this, "Download Started...", Toast.LENGTH_LONG).show();

                    try {
                        downloadTask(strNew3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.GONE);
                }
            });

            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(TAG, "Ad dismissed fullscreen content.");
                    mRewardedAd = null;
//                    setAds();

                }
            });

        } else {


            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);
            Log.d(TAG, "The rewarded ad wasn't ready yet.I'm Calling Again");
            Toast.makeText(this, "No Ads available right now. Try after some time", Toast.LENGTH_LONG).show();
        }

//        IronSource.showRewardedVideo("DefaultRewardedVideo");
//        IronSource.setRewardedVideoListener(new RewardedVideoListener() {
//
//            @Override
//            public void onRewardedVideoAdOpened() {
//            }
//
//            @Override
//            public void onRewardedVideoAdClosed() {
//            }
//
//            @Override
//            public void onRewardedVideoAvailabilityChanged(boolean available) {
//                //Change the in-app 'Traffic Driver' state according to availability.
//            }
//
//            @Override
//            public void onRewardedVideoAdRewarded(Placement placement) {
//                // gift of users
//                String rewardName = placement.getRewardName();
//                rewardAmount = placement.getRewardAmount();
//                Toast.makeText(MainActivity.this, "Download Started...", Toast.LENGTH_LONG).show();
//
//                try {
//                    downloadTask(strNew3);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                progressBar.setProgress(0);
//                progressBar.setVisibility(View.GONE);
//            }

//            @Override
//            public void onRewardedVideoAdShowFailed(IronSourceError error) {
//                StartAppAd.showAd(getApplicationContext());
//                //loadreward();  uncoment when adlimit gone
//
//                try {
//                    downloadTask(strNew3);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                progressBar.setProgress(0);
//                progressBar.setVisibility(View.GONE);
//
//
//            }
//
//            /*Invoked when the end user clicked on the RewardedVideo ad
//             */
//            @Override
//            public void onRewardedVideoAdClicked(Placement placement) {
//
//
//            }
//
//            @Override
//            public void onRewardedVideoAdStarted() {
//
//
//            }
//
//            /* Invoked when the video ad finishes plating. */
//            @Override
//            public void onRewardedVideoAdEnded() {
//
//            }
//        });

    }


    private class WebViewClientDemo extends WebViewClient {


        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            AlertDialog alertDialog = builder.create();
            String message = "SSL Certificate error.";
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = "The certificate authority is not trusted.";
                    break;
                case SslError.SSL_EXPIRED:
                    message = "The certificate has expired.";
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = "The certificate Hostname mismatch.";
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = "The certificate is not yet valid.";
                    break;
            }

            message += " Do you want to continue anyway?";
            alertDialog.setTitle("SSL Certificate Error");
            alertDialog.setMessage(message);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Ignore SSL certificate errors
                    handler.proceed();
                }
            });

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    handler.cancel();
                }
            });
            alertDialog.show();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("TAG", "onPageStarted: ");
        }


        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//                Log.d("TAG", "onPageFinished: method ");
//                if (view != null && !TextUtils.isEmpty(view.getTitle())) {
//                    Log.d("TAG", "onPageFinished: " + view.getTitle());
//                } else {
//                    Log.d("TAG", "onPageFinished: now reload");
//                    view.reload();
//                }
        }


        private boolean shouldProceed(SslError error) {
            return isCertificateValid(error);
        }

        private boolean isCertificateValid(SslError error) {
            if (error != null) {
                int primaryError = error.getPrimaryError();
                if (primaryError == SslError.SSL_UNTRUSTED) {
                    // Certificate is not trusted, handle accordingly
                    return false;
                }
            }
            // Certificate is valid or error is not SSL_UNTRUSTED
            return true;
        }


    }

    protected void onResume() {
        super.onResume();
//        IronSource.onResume(this);
    }

    protected void onPause() {
        super.onPause();
//        IronSource.onPause(this);
    }

    //set back button functionality
    @Override
    public void onBackPressed() { //if user presses the back button do this
        if (webview.isFocused() && webview.canGoBack()) { //check if in webview and the user can go back
            webview.goBack(); //go back in webview
        } else { //do this if the webview cannot go back any further
            // review
            manager = ReviewManagerFactory.create(MainActivity.this);
            Task<ReviewInfo> request1 = manager.requestReviewFlow();
            request1.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                @Override
                public void onComplete(@NonNull Task<ReviewInfo> task) {

                    if (task.isSuccessful()) {
                        reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(MainActivity.this, reviewInfo);

                        flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void result) {

                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // review end

            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                return;
            } else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }

    }

    private void netcheck() {

        if (!CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            webview = findViewById(R.id.webView);
            webview.loadUrl("file:///android_asset/error.html");
        }

    }

    private void loadu() {
        webview = findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                webview.loadUrl("file:///android_asset/error.html");
            }
        });
    }

    private TrustManagerFactory getTrustManagerFactory(KeyStore trustedCertificate) {
        TrustManagerFactory trustManagerFactory = null;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustedCertificate);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return trustManagerFactory;
    }


//    private class Myclient2 extends WebViewClient {
//
//        @Override
//        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
//
//            final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getApplicationContext());
//            String message = "SSL Certificate error.";
//            switch (error.getPrimaryError()) {
//                case SslError.SSL_UNTRUSTED:
//                    message = "The certificate authority is not trusted.";
//                    break;
//                case SslError.SSL_EXPIRED:
//                    message = "The certificate has expired.";
//                    break;
//                case SslError.SSL_IDMISMATCH:
//                    message = "The certificate Hostname mismatch.";
//                    break;
//                case SslError.SSL_NOTYETVALID:
//                    message = "The certificate is not yet valid.";
//                    break;
//            }
//            message += " Do you want to continue anyway?";
//
//            builder.setTitle("SSL Certificate Error");
//            builder.setMessage(message);
//            builder.setPositiveButton("continue", (dialog, which) -> handler.proceed());
//            builder.setNegativeButton("cancel", (dialog, which) -> handler.cancel());
//            final androidx.appcompat.app.AlertDialog dialog = builder.create();
//            dialog.show();
//
//
////            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
////            builder.setMessage(R.string.notification_error_ssl_cert_invalid);
////            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    handler.proceed();
////                }
////            });
////            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    handler.cancel();
////                }
////            });
////            final AlertDialog dialog = builder.create();
////            dialog.show();
//        }
//
//    }
}


