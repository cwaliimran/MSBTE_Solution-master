package com.msbte.modelanswerpaper

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MicroProjectActivity : AppCompatActivity() {
    lateinit var recview: RecyclerView
    lateinit var adapter: MyProjectDownloadAdapter
    private lateinit var toolbar: Toolbar
    private var mData = mutableListOf<CommonModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_microproject)
        toolbar = findViewById(R.id.mytoolbar)
        toolbar.title = getString(R.string.projects)
        setSupportActionBar(toolbar)
// Handle the back button click
        // Handle the back button click
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        Toast.makeText(this, "Loading Files...", Toast.LENGTH_LONG).show()
        MobileAds.initialize(this) { }

        //Runtime External storage permission for saving download files
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it")
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, 1)
            }
        }
        recview = findViewById(R.id.recyclerViewcpdf) ?: RecyclerView(this)
//        val options = FirebaseRecyclerOptions.Builder<model>()
//            .setQuery(FirebaseDatabase.getInstance().reference.child("project"), model::class.java)
//            .build()
        adapter = MyProjectDownloadAdapter(mData)
        recview.adapter = adapter

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("project")


        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the existing list to avoid duplicates in case onDataChange is triggered multiple times
                mData.clear()

                // Loop through all the children of the "project" node
                for (snapshot in dataSnapshot.children) {
                    // Deserialize the data into your model class
                    val commonModelData = snapshot.getValue(CommonModel::class.java)

                    if (commonModelData != null) {
                        mData.add(commonModelData)
                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error if any occurs
            }
        })
    }
}