package com.msbte.modelanswerpaper.fragment_ui;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.msbte.modelanswerpaper.CheckNetwork;
import com.msbte.modelanswerpaper.R;
import com.msbte.modelanswerpaper.databinding.FragmentQuestionPaperViewerBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionPaperViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionPaperViewerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentQuestionPaperViewerBinding binding;
    private String url;
    ReviewManager manager;
    ReviewInfo reviewInfo;

    @SuppressLint("WebViewClientOnReceivedSslError")
    @SuppressWarnings("unchecked")

    public QuestionPaperViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionPaperViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionPaperViewerFragment newInstance(String param1, String param2) {
        QuestionPaperViewerFragment fragment = new QuestionPaperViewerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // review
        // In onCreateView or onViewCreated method of your Fragment
        manager = ReviewManagerFactory.create(getActivity());
        Task<ReviewInfo> request = manager.requestReviewFlow();
        Log.d(TAG, "called");
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get ReviewInfo object
                reviewInfo = task.getResult();
                Log.d(TAG, "called2");
                // Launch review flow
                Task<Void> flow = manager.launchReviewFlow(getActivity(), reviewInfo);
                flow.addOnCompleteListener(reviewFlowTask -> {
                    if (reviewFlowTask.isSuccessful()) {

                    } else {

                    }
                });
            } else {
                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        // review end

        // Inflate the layout for this fragment
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question_paper_viewer, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        netcheck();
        backPressListner();
        setupAdapter();
        setupAds();
        loadData();
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    private void setupAds() {
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        if (binding.adView2Main != null) {
            binding.adView2Main.loadAd(adRequest);
        }
    }

    private void loadData() {
        binding.tvTitle.setText(mParam1);

        if (TextUtils.isEmpty(mParam2)) {
            return;
        }
        try {
            url = URLEncoder.encode(mParam2, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        binding.progressBar.setVisibility(View.VISIBLE);


        netcheck();
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        binding.webView.clearSslPreferences();
        //Those other methods I tried out of despair just in case
        binding.webView.clearFormData();
        binding.webView.clearCache(true);
        binding.webView.clearHistory();
        binding.webView.clearMatches();
        binding.webView.setWebViewClient(new SSLTolerentWebViewClient());


        binding.webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        // Add SSL related configurations
        binding.webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE); // Allow loading mixed content
        binding.webView.getSettings().setBlockNetworkLoads(false); // Allow network loads


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

        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                binding.progressBar.setVisibility(View.VISIBLE);
                if (newProgress >= 75) {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (error != null) {
                    Log.d("TAG", "onReceivedError: " + error.toString());
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("TAG", "onPageStarted: ");
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

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

//            @Override
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

        });

        binding.webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
    }

    private void setupAdapter() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(binding.getRoot()).popBackStack();

            }
        });

        if (TextUtils.isEmpty(mParam1)) {
            return;
        }
        binding.tvTitle.setText(mParam1);
        if (TextUtils.isEmpty(mParam2)) {
            return;
        }
    }

    private void netcheck() {

        if (!CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
        {
            binding.webView.loadUrl("file:///android_asset/error.html");
        }

    }

    private void backPressListner() {

        getActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Navigation.findNavController(binding.getRoot()).popBackStack();
                    }
                });


    }


    private class SSLTolerentWebViewClient extends WebViewClient {
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    }

//    public class MyclientWebView extends WebViewClient {
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


}