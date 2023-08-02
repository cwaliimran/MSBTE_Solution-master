package com.msbte.modelanswerpaper.fragment_ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.msbte.modelanswerpaper.CheckNetwork
import com.msbte.modelanswerpaper.R
import com.msbte.modelanswerpaper.databinding.FragmentQuestionPaperViewerBinding
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionPaperViewerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionPaperViewerFragment @SuppressLint("WebViewClientOnReceivedSslError") constructor() :
    Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var mParam1: String
    private lateinit var mParam2: String
    private lateinit var binding: FragmentQuestionPaperViewerBinding
    private lateinit var url: String
    lateinit var manager: ReviewManager
    lateinit var reviewInfo: ReviewInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1).toString()
            mParam2 = requireArguments().getString(ARG_PARAM2).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // review
        // In onCreateView or onViewCreated method of your Fragment
        manager = ReviewManagerFactory.create(requireActivity())
        val request = manager!!.requestReviewFlow()
        Log.d(ContentValues.TAG, "called")
        request.addOnCompleteListener { task: Task<ReviewInfo?> ->
            if (task.isSuccessful) {
                // Get ReviewInfo object
                reviewInfo = task.result
                Log.d(ContentValues.TAG, "called2")
                // Launch review flow
                val flow = manager!!.launchReviewFlow(
                    requireActivity(), reviewInfo!!
                )
                flow.addOnCompleteListener { reviewFlowTask: Task<Void?> ->
                    if (reviewFlowTask.isSuccessful) {
                    } else {
                    }
                }
            } else {
                Toast.makeText(activity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        }
        // review end

        // Inflate the layout for this fragment
        if (binding == null) binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_question_paper_viewer,
            container,
            false
        )
        binding!!.lifecycleOwner = viewLifecycleOwner
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        netcheck()
        backPressListner()
        setupAdapter()
        setupAds()
        loadData()
        binding!!.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupAds() {
        MobileAds.initialize(requireContext()) { }
        val adRequest = AdRequest.Builder().build()
        if (binding!!.adView2Main != null) {
            binding!!.adView2Main.loadAd(adRequest)
        }
    }

    private fun loadData() {
        binding!!.tvTitle.text = mParam1
        if (TextUtils.isEmpty(mParam2)) {
            return
        }
        try {
            url = URLEncoder.encode(mParam2, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        binding!!.progressBar.visibility = View.VISIBLE
        netcheck()
        binding!!.webView.settings.javaScriptEnabled = true
        binding!!.webView.settings.domStorageEnabled = true
        binding!!.webView.settings.loadsImagesAutomatically = true
        binding!!.webView.settings.builtInZoomControls = true
        binding!!.webView.settings.useWideViewPort = true
        binding!!.webView.settings.allowFileAccessFromFileURLs = true
        binding!!.webView.settings.allowUniversalAccessFromFileURLs = true
        binding!!.webView.clearSslPreferences()
        //Those other methods I tried out of despair just in case
        binding!!.webView.clearFormData()
        binding!!.webView.clearCache(true)
        binding!!.webView.clearHistory()
        binding!!.webView.clearMatches()
        binding!!.webView.webViewClient = SSLTolerentWebViewClient()
        binding!!.webView.overScrollMode = WebView.OVER_SCROLL_NEVER

        // Add SSL related configurations
        binding!!.webView.settings.mixedContentMode =
            WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE // Allow loading mixed content
        binding!!.webView.settings.blockNetworkLoads = false // Allow network loads


//        binding.webView.setWebViewClient(new MyclientWebView());

//        binding.webView.getSettings().setJavaScriptEnabled(true);
//        binding.webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 3.0; en-us; Xoom Build/HRI39) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13");
//        binding.webView.getSettings().setLoadWithOverviewMode(true);
//        binding.webView.getSettings().setUseWideViewPort(true);
//        binding.webView.getSettings().setAllowFileAccess(true);
//        binding.webView.getSettings().setAllowContentAccess(true);
//        binding.webView.getSettings().setBuiltInZoomControls(true);
//        binding.webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        binding.webView.getSettings().setDomStorageEnabled(true);
        binding!!.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding!!.progressBar.visibility = View.VISIBLE
                if (newProgress >= 75) {
                    binding!!.progressBar.visibility = View.GONE
                }
            }
        }
        binding!!.webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                if (error != null) {
                    Log.d("TAG", "onReceivedError: $error")
                }
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                Log.d("TAG", "onPageStarted: ")
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

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            } //            @Override
            //            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //                // Check the error code.
            //                int errorCode = error.getPrimaryError();
            //
            //                if (errorCode == SslError.SSL_UNTRUSTED) {
            //                    // SSL certificate is untrusted
            //                    Log.d("TAG", "SSL Error: Untrusted");
            //                } else if (errorCode == SslError.SSL_EXPIRED) {
            //                    // SSL certificate has expired
            //                    Log.d("TAG", "SSL Error: Expired");
            //                } else if (errorCode == SslError.SSL_IDMISMATCH) {
            //                    // Hostname mismatch with SSL certificate
            //                    Log.d("TAG", "SSL Error: Hostname Mismatch");
            //                } else if (errorCode == SslError.SSL_NOTYETVALID) {
            //                    // SSL certificate is not yet valid
            //                    Log.d("TAG", "SSL Error: Not Yet Valid");
            //                } else {
            //                    // Other SSL errors
            //                    Log.d("TAG", "SSL Error: " + errorCode);
            //                }
            //                // Proceed with the connection (may not be secure)
            //                handler.proceed();
            //
            //            }
        }
        binding!!.webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$url")
    }

    private fun setupAdapter() {
        binding!!.imgBack.setOnClickListener { findNavController(binding!!.root).popBackStack() }
        if (TextUtils.isEmpty(mParam1)) {
            return
        }
        binding!!.tvTitle.text = mParam1
        if (TextUtils.isEmpty(mParam2)) {
            return
        }
    }

    private fun netcheck() {
        if (!CheckNetwork.isInternetAvailable(activity)) //returns true if internet available
        {
            binding!!.webView.loadUrl("file:///android_asset/error.html")
        }
    }

    private fun backPressListner() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController(binding!!.root).popBackStack()
                }
            })
    }

    private inner class SSLTolerentWebViewClient : WebViewClient() {
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            val builder = AlertDialog.Builder(
                requireActivity()
            )
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
                DialogInterface.BUTTON_POSITIVE,
                "OK"
            ) { dialog, which -> // Ignore SSL certificate errors
                handler.proceed()
            }
            alertDialog.setButton(
                DialogInterface.BUTTON_NEGATIVE,
                "Cancel"
            ) { dialog, which -> handler.cancel() }
            alertDialog.show()
        }
    } //    public class MyclientWebView extends WebViewClient {

    //
    //        @Override
    //        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
    //
    //            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
    //            final AlertDialog dialog = builder.create();
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
    //    }
    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuestionPaperViewerFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): QuestionPaperViewerFragment {
            val fragment = QuestionPaperViewerFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}