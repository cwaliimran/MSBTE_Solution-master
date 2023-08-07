package com.msbte.modelanswerpaper

import android.Manifest
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class Download : AppCompatActivity() {
    lateinit var name: String
    lateinit var fileeurl: String
    var rewardAmount = 0
    lateinit var redown: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        name = intent.getStringExtra("filename").toString()
        fileeurl = intent.getStringExtra("fileurl").toString()
        val intent = intent

//        IronSource.init(download.this, "1201f8ea5", IronSource.AD_UNIT.REWARDED_VIDEO);

        //Runtime External storage permission for saving download files
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
            ) {
                Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it")
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, 1)
            }
        }
        redown = findViewById(R.id.redownload)
        AlertDialog.Builder(this@Download) //alert the person knowing they are about to close
            .setTitle("Watch Ad To Download")
            .setMessage("✅ You Need to Watch Full Video Ad to Download File")
            .setPositiveButton("Watch Ad") { dialog, which ->
                loadreward()
                Toast.makeText(
                    this@Download,
                    "Ad is Loading Please wait a movement... ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("No") { dialog, which -> }
            .show()
        redown.setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(this@Download) //alert the person knowing they are about to close
                .setTitle("Watch Ad To Download")
                .setMessage("✅ You Need to Watch Full Video Ad to Download File")
                .setPositiveButton("Watch Ad") { dialog, which ->
                    loadreward()
                    Toast.makeText(
                        this@Download,
                        "Ad is Loading Please wait a movement... ",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .setNegativeButton("No") { dialog, which -> }
                .show()
        })
    }

    private fun loadreward() {


        // gift of users
        try {
            downloadTask(fileeurl, name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    private fun downloadTask(url: String?, name2: String?): Boolean {
        if (!url!!.startsWith("http")) {
            return false
        }
        //        String name = "MSBTE_Solution_App.pdf";
        try {
            val file = File(Environment.getExternalStorageDirectory(), "Download")
            if (!file.exists()) {
                file.mkdirs()
            }
            val result = File(file.absolutePath + File.separator + name2)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            request.setDestinationUri(Uri.fromFile(result))
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            downloadManager?.enqueue(request)
            //mToast(mContext, "Starting download...");
            MediaScannerConnection.scanFile(
                this@Download, arrayOf(result.toString()), null
            ) { path, uri -> }
        } catch (e: Exception) {
            Log.e(">>>>>", e.toString())
            //mToast(this, e.toString());
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        //        IronSource.onResume(this);
    }

    override fun onPause() {
        super.onPause()
        //        IronSource.onPause(this);
    }
}