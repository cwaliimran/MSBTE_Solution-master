package com.msbte.modelanswerpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



import java.io.File;

public class download extends AppCompatActivity {

    String name,fileeurl;
    int rewardAmount = 0;
    Button redown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        name = getIntent().getStringExtra("filename");
        fileeurl = getIntent().getStringExtra("fileurl");
        Intent intent = getIntent();

//        IronSource.init(download.this, "1201f8ea5", IronSource.AD_UNIT.REWARDED_VIDEO);

        //Runtime External storage permission for saving download files
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            }
        }

        redown = findViewById(R.id.redownload);

        new AlertDialog.Builder(download.this) //alert the person knowing they are about to close
                .setTitle("Watch Ad To Download")
                .setMessage("✅ You Need to Watch Full Video Ad to Download File")
                .setPositiveButton("Watch Ad", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                    loadreward();
                        Toast.makeText(download.this, "Ad is Loading Please wait a movement... ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

        redown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(download.this) //alert the person knowing they are about to close
                        .setTitle("Watch Ad To Download")
                        .setMessage("✅ You Need to Watch Full Video Ad to Download File")
                        .setPositiveButton("Watch Ad", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadreward();
                                Toast.makeText(download.this, "Ad is Loading Please wait a movement... ", Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    private void loadreward() {




                // gift of users
                try {
                    downloadTask(fileeurl,name);
                } catch (Exception e) {
                    e.printStackTrace();
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
//                try {
//                    downloadTask(fileeurl,name);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onRewardedVideoAdShowFailed(IronSourceError error) {
//                StartAppAd.showAd(getApplicationContext());
//                rewardAmount = 100;
//                try {
//                    downloadTask(fileeurl,name);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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

    private boolean downloadTask(String url,String name2) throws Exception {
        if (!url.startsWith("http")) {
            return false;
        }
//        String name = "MSBTE_Solution_App.pdf";
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Download");
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.mkdirs();
            }
            File result = new File(file.getAbsolutePath() + File.separator + name2);
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setDestinationUri(Uri.fromFile(result));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
            }
            //mToast(mContext, "Starting download...");
            MediaScannerConnection.scanFile(download.this, new String[]{result.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } catch (Exception e) {
            Log.e(">>>>>", e.toString());
            //mToast(this, e.toString());
            return false;
        }
        return true;
    }


    protected void onResume() {
        super.onResume();
//        IronSource.onResume(this);

    }

    protected void onPause() {
        super.onPause();
//        IronSource.onPause(this);

    }
}