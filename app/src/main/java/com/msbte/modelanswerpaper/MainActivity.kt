package com.msbte.modelanswerpaper

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.ContentValues
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaScannerConnection
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var websiteURL: String
    lateinit var strNew3: String
    var rewardAmount = 0
    private lateinit var webview: WebView
    private lateinit var progressBar: ProgressBar
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    private lateinit var mAdView: AdView
    private var mRewardedAd: RewardedAd? = null
    lateinit var layout: CoordinatorLayout
    lateinit var manager: ReviewManager
    lateinit var reviewInfo: ReviewInfo

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.layout)

        MobileAds.initialize(this) { }

//        IronSource.init(MainActivity.this, "1201f8ea5", IronSource.AD_UNIT.REWARDED_VIDEO);
        mAdView = findViewById(R.id.adView2_main)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {

        }

//        setAds();
        websiteURL = intent.getStringExtra("url").toString()
        val intent = intent
        progressBar = findViewById(R.id.progress_bar)
        progressBar.max = 100
        progressBar.progressDrawable.setColorFilter(
            Color.RED, PorterDuff.Mode.SRC_IN
        )
        netcheck()
        if (!CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            //if there is no internet do this
            setContentView(R.layout.activity_main)
            //Toast.makeText(this,"No Internet Connection, Chris",Toast.LENGTH_LONG).show();
            AlertDialog.Builder(this) //alert the person knowing they are about to close
                .setTitle("No internet connection available")
                .setMessage("Please Check you're Mobile data or Wifi network.")
                .setPositiveButton("Exit") { dialog, which -> finish() } //.setNegativeButton("No", null)
                .show()
        } else {
            //Webview stuff
            webview = findViewById(R.id.webView)
            webview.webViewClient = WebViewClientDemo()
            webview.settings.javaScriptEnabled = true
            webview.settings.domStorageEnabled = true
            webview.settings.loadsImagesAutomatically = true
            webview.settings.builtInZoomControls = true
            webview.settings.useWideViewPort = true
            webview.settings.allowFileAccessFromFileURLs = true
            webview.settings.allowUniversalAccessFromFileURLs = true
            webview.clearSslPreferences()
            webview.overScrollMode = WebView.OVER_SCROLL_NEVER

            //Those other methods I tried out of despair just in case
            webview.clearFormData()
            webview.clearCache(true)
            webview.clearHistory()
            webview.clearMatches()


            // Add SSL related configurations
            webview.settings.mixedContentMode =
                WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE // Allow loading mixed content
            webview.settings.blockNetworkLoads = false // Allow network loads

            webview.webViewClient = WebViewClient()
            webview.loadUrl("file:///android_asset/drive_embed.html")

            //progressBar.setProgress(0)
            netcheck()

        }

        //Runtime External storage permission for saving download files
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it")
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, 1)
            }
        }
        netcheck()
        try {
            webview.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
                netcheck()

//                    String webUrl = webview.getUrl();
//
//                    String substring = "file/d";
//                    boolean reuslt = webUrl.contains(substring);
//
//                    String substring2 = "econtent.msbte";
//                    boolean reuslt2 = webUrl.contains(substring2);
                AlertDialog.Builder(this@MainActivity) //alert the person knowing they are about to close
                    .setTitle("Attention all users \uD83D\uDEAB")
                    .setMessage("The downloading feature was closed. We apologize for any inconvenience caused.")
                    .setPositiveButton("Ok") { dialog, which ->
                        progressBar.progress = 0
                        progressBar.visibility = View.GONE
                    }.show()
            }
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Something Went Wrong!", Toast.LENGTH_LONG).show()
            Toast.makeText(
                this@MainActivity, "Please Check Your Internet Connection...", Toast.LENGTH_LONG
            ).show()
        }
        netcheck()
    }

    @Throws(Exception::class)
    private fun downloadTask(url: String?): Boolean {
        if (!url!!.startsWith("http")) {
            return false
        }
        val name = "MSBTE_Solution_App.pdf"
        try {
            val file = File(Environment.getExternalStorageDirectory(), "Download")
            if (!file.exists()) {
                file.mkdirs()
            }
            val result = File(file.absolutePath + File.separator + name)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            request.setDestinationUri(Uri.fromFile(result))
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            downloadManager.enqueue(request)
            //mToast(mContext, "Starting download...");
            MediaScannerConnection.scanFile(
                this@MainActivity, arrayOf(result.toString()), null
            ) { path, uri -> }
        } catch (e: Exception) {
            Log.e(">>>>>", e.toString())
            return false
        }
        return true
    }

    //load ads
    private fun setAds() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            this,
            getString(R.string.reward_id),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error.
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                }
            })
    }

    // show ads
    private fun loadreward() {
        if (mRewardedAd != null) {
            mRewardedAd!!.show(this@MainActivity) { rewardItem ->
                val checkad = rewardItem.amount.toString()
                val rewardType = rewardItem.type

                // gift of users
//                    String rewardName = placement.getRewardName();
//                    rewardAmount = placement.getRewardAmount();
                Toast.makeText(this@MainActivity, "Download Started...", Toast.LENGTH_LONG).show()
                try {
                    downloadTask(strNew3)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                progressBar.progress = 0
                progressBar.visibility = View.GONE
            }
            mRewardedAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(ContentValues.TAG, "Ad dismissed fullscreen content.")
                    mRewardedAd = null
                    //                    setAds();
                }
            }
        } else {
            progressBar.progress = 0
            progressBar.visibility = View.GONE
            Log.d(ContentValues.TAG, "The rewarded ad wasn't ready yet.I'm Calling Again")
            Toast.makeText(
                this, "No Ads available right now. Try after some time", Toast.LENGTH_LONG
            ).show()
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

    private inner class WebViewClientDemo : WebViewClient() {
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            val builder = AlertDialog.Builder(this@MainActivity)
            val alertDialog = builder.create()
            var message = "SSL Certificate error."
            when (error.primaryError) {
                SslError.SSL_UNTRUSTED -> message = "The certificate authority is not trusted."
                SslError.SSL_EXPIRED -> message = "The certificate has expired."
                SslError.SSL_IDMISMATCH -> message = "The certificate Hostname mismatch."
                SslError.SSL_NOTYETVALID -> message = "The certificate is not yet valid."
            }
            message += " Do you want to continue anyway?"
            alertDialog.setTitle("SSL Certificate Error")
            alertDialog.setMessage(message)
            alertDialog.setButton(
                DialogInterface.BUTTON_POSITIVE, "OK"
            ) { dialog, which -> // Ignore SSL certificate errors
                handler.proceed()
            }
            alertDialog.setButton(
                DialogInterface.BUTTON_NEGATIVE, "Cancel"
            ) { dialog, which -> handler.cancel() }
            alertDialog.show()
        }


        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }


        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            webview.loadUrl(websiteURL)
            return true
        }


        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            //                Log.d("TAG", "onPageFinished: method ");
//                if (view != null && !TextUtils.isEmpty(view.getTitle())) {
//                    Log.d("TAG", "onPageFinished: " + view.getTitle());
//                } else {
//                    Log.d("TAG", "onPageFinished: now reload");
//                    view.reload();
//                }
        }

        private fun shouldProceed(error: SslError): Boolean {
            return isCertificateValid(error)
        }

        private fun isCertificateValid(error: SslError?): Boolean {
            if (error != null) {
                val primaryError = error.primaryError
                if (primaryError == SslError.SSL_UNTRUSTED) {
                    // Certificate is not trusted, handle accordingly
                    return false
                }
            }
            // Certificate is valid or error is not SSL_UNTRUSTED
            return true
        }
    }

    //set back button functionality
    override fun onBackPressed() { //if user presses the back button do this
        if (webview.isFocused && webview.canGoBack()) { //check if in webview and the user can go back
            webview.goBack() //go back in webview
        } else { //do this if the webview cannot go back any further
            // review
            manager = ReviewManagerFactory.create(this@MainActivity)
            val request1 = manager.requestReviewFlow()
            request1.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reviewInfo = task.result
                    val flow = manager.launchReviewFlow(this@MainActivity, reviewInfo)
                    flow.addOnSuccessListener { }
                } else {
                    Toast.makeText(this@MainActivity, "Something Went Wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            // review end
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel()
                super.onBackPressed()
                return
            } else {
                backToast =
                    Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT)
                backToast.show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }

    private fun netcheck() {
        if (!CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            webview = findViewById(R.id.webView)
            webview.loadUrl("file:///android_asset/error.html")
        }
    }

    private fun loadu() {
        webview = findViewById(R.id.webView)
        webview.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView, request: WebResourceRequest, error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                webview.loadUrl("file:///android_asset/error.html")
            }
        }
    }

//    private fun getTrustManagerFactory(trustedCertificate: KeyStore): TrustManagerFactory? {
//        var trustManagerFactory: TrustManagerFactory
//        try {
//            trustManagerFactory =
//                TrustManagerFactory.getInstance(TrustManagerpFactory.getDefaultAlgorithm())
//            trustManagerFactory.init(trustedCertificate)
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        } catch (e: KeyStoreException) {
//            e.printStackTrace()
//        }
//        return trustManagerFactory
//    }

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