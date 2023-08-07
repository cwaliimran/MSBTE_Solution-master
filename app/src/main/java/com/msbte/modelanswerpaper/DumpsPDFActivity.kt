package com.msbte.modelanswerpaper

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.DownloadListener
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DumpsPDFActivity : AppCompatActivity() {
    lateinit var websiteURL: String
    lateinit var strNew3: String
    var rewardAmount = 0
    private lateinit var webview: WebView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dumps_pdfactivity)

        // To prevent an Android application from taking screenshots and recording the screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE
        )

        // prevent the keyboard from appearing
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        progressBar = findViewById(R.id.progress_bar5)
        webview = findViewById(R.id.webView5)
        progressBar.max = 100
        progressBar.progressDrawable.setColorFilter(
            Color.RED, PorterDuff.Mode.SRC_IN
        )
        netcheck()
        websiteURL = intent.getStringExtra("url").toString()
        val intent = intent
        if (!CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            //if there is no internet do this
            setContentView(R.layout.activity_dumps_pdfactivity)
            //Toast.makeText(this,"No Internet Connection, Chris",Toast.LENGTH_LONG).show();
            AlertDialog.Builder(this) //alert the person knowing they are about to close
                .setTitle("No internet connection available")
                .setMessage("Please Check you're Mobile data or Wifi network.")
                .setPositiveButton("Exit") { dialog, which -> finish() } //.setNegativeButton("No", null)
                .show()
        } else {
            //Webview stuff
            webview.webViewClient = WebViewClientDemo()
            webview.settings.javaScriptEnabled = true
            webview.settings.domStorageEnabled = true
            webview.settings.loadsImagesAutomatically = true
            webview.settings.builtInZoomControls = true
            webview.settings.useWideViewPort = true
            webview.overScrollMode = WebView.OVER_SCROLL_NEVER
            webview.loadUrl(websiteURL)
            webview.loadUrl(websiteURL)
            progressBar.progress = 0
            netcheck()
            webview.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    progressBar.progress = newProgress
                    if (newProgress == 100) progressBar.visibility =
                        View.GONE else progressBar.visibility =
                        View.VISIBLE
                    netcheck()
                    super.onProgressChanged(view, newProgress)
                }
            }
            netcheck()
        }
        try {
            webview.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
                netcheck()

//                    String webUrl = webview.getUrl();
//
//                    String substring = "file/d";
//                    boolean reuslt = webUrl.contains(substring);
//
//                    String substring2 = "econtent.msbte";
//                    boolean reuslt2 = webUrl.contains(substring2);
                AlertDialog.Builder(this@DumpsPDFActivity) //alert the person knowing they are about to close
                    .setTitle("Attention all users \uD83D\uDEAB")
                    .setMessage("The downloading feature was closed. We apologize for any inconvenience caused.")
                    .setPositiveButton("Ok") { dialog, which ->
                        progressBar.progress = 0
                        progressBar.visibility = View.GONE
                    }.show()
            })
        } catch (e: Exception) {
            Toast.makeText(this@DumpsPDFActivity, "Something Went Wrong!", Toast.LENGTH_LONG).show()
            Toast.makeText(
                this@DumpsPDFActivity, "Please Check Your Internet Connection...", Toast.LENGTH_LONG
            ).show()
        }
        netcheck()
    }

    private inner class WebViewClientDemo : WebViewClient() {
        //Keep webview in app when clicking links
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            webview.loadUrl(websiteURL)
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
        }
    }

    private fun netcheck() {
        if (!CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            webview = findViewById(R.id.webView)
            webview.loadUrl("file:///android_asset/error.html")
        }
    }
}