package com.msbte.modelanswerpaper

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.msbte.modelanswerpaper.interfaces.OnItemClick

class HomeActivity : AppCompatActivity() {
    lateinit var gridView: RecyclerView
    lateinit var intenttransf: Intent
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    private lateinit var eurl: String
    private lateinit var toolbar: Toolbar
    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolbar = findViewById(R.id.mytoolbar)
        setSupportActionBar(toolbar)
        MobileAds.initialize(this) { }
        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        val slideModels: MutableList<SlideModel> = ArrayList()
        slideModels.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/msbte-solution.appspot.com/o/slider%2F1.png?alt=media&token=b8a3e1f5-74e4-4485-9d9b-6fbfd02714d8",
                ScaleTypes.FIT
            )
        )
        /* ad1 */slideModels.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/msbte-solution.appspot.com/o/slider%2F2.png?alt=media&token=c85167af-1fa9-48a3-8add-876df8f22efc",
                ScaleTypes.FIT
            )
        )
        /* ad2 */slideModels.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/msbte-solution.appspot.com/o/slider%2F4.png?alt=media&token=689f16dd-b6e9-4efa-a9cc-116da0bbce83",
                ScaleTypes.FIT
            )
        )
        slideModels.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/msbte-solution.appspot.com/o/slider%2F5.png?alt=media&token=73d1e962-960c-4c80-84df-8282a14f527f",
                ScaleTypes.FIT
            )
        )
        imageSlider.setImageList(slideModels)
        setAds()
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(i: Int) {
                Log.d(ContentValues.TAG, "Selected item $i")
                when (i) {
                    0 -> startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://bit.ly/3Tf1ZQe")
                        )
                    )

                    1 -> startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://bit.ly/3RLmfrm")
                        )
                    )

                    2 -> startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://bit.ly/3ekpisP")
                        )
                    )

                    3 -> startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://bit.ly/3CPRgq4")
                        )
                    )
                }
            }
        })
//        inAppUpdateManager = InAppUpdateManager.Builder(this, 101)
//            .resumeUpdates(true)
//            .mode(Constants.UpdateMode.IMMEDIATE) // Immdiate Means Forcefully Install And Felexiable means no thanks option
//            .snackBarAction("An update has just been downloaded")
//            .snackBarAction("RESTART")
//            .handler(this)
//        inAppUpdateManager.checkForAppUpdate()
        gridView = findViewById<View>(R.id.gridView1) as RecyclerView
        gridView!!.adapter = ImageAdapter(MOBILE_OS, object : OnItemClick {
            override fun onClick(position: Int, type: String?, data: Any?) {
                val name = data.toString()
                when (name) {
                    "Model Answer" -> if (mInterstitialAd != null) {
                        mInterstitialAd!!.show(this@HomeActivity)
                        mInterstitialAd!!.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    intenttransf =
                                        Intent(applicationContext, MainActivity::class.java)
                                    eurl =
                                        "https://drive.google.com/drive/folders/1t_lRCTROxXT8XdplqgIf64c31f1vqXoD?usp=sharing"
                                    intenttransf.putExtra("url", eurl)
                                    startActivity(intenttransf)
                                    mInterstitialAd = null
                                    //                                    setAds();
                                }
                            }
                    } else {
                        intenttransf = Intent(applicationContext, MainActivity::class.java)
                        eurl =
                            "https://drive.google.com/drive/folders/1t_lRCTROxXT8XdplqgIf64c31f1vqXoD?usp=sharing"
                        intenttransf!!.putExtra("url", eurl)
                        startActivity(intenttransf)
                        //                            setAds();
                    }

                    "Projects" -> if (mInterstitialAd != null) {
                        mInterstitialAd!!.show(this@HomeActivity)
                        mInterstitialAd!!.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    intenttransf =
                                        Intent(
                                            applicationContext,
                                            MicroProjectActivity::class.java
                                        )
                                    startActivity(intenttransf)
                                    mInterstitialAd = null
                                    //                                    setAds();
                                }
                            }
                    } else {
                        intenttransf = Intent(applicationContext, MicroProjectActivity::class.java)
                        startActivity(intenttransf)
                        //                            setAds();
                    }

                    "MSBTE Content", "Question Paper" -> if (mInterstitialAd != null) {
                        mInterstitialAd!!.show(this@HomeActivity)
                        mInterstitialAd!!.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    intenttransf =
                                        Intent(applicationContext, NewFeatureActivity::class.java)
                                    startActivity(intenttransf)
                                    mInterstitialAd = null
                                    //                                    setAds();
                                }
                            }
                    } else {
                        intenttransf = Intent(applicationContext, NewFeatureActivity::class.java)
                        startActivity(intenttransf)
                        //                            setAds();
                    }

                    "MSBTE Solution Pro" -> if (mInterstitialAd != null) {
                        mInterstitialAd!!.show(this@HomeActivity)
                        mInterstitialAd!!.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    intenttransf =
                                        Intent(applicationContext, DumpsActivity::class.java)
                                    startActivity(intenttransf)
                                    mInterstitialAd = null
                                    //                                    setAds();
                                }
                            }
                    } else {
                        intenttransf = Intent(applicationContext, DumpsActivity::class.java)
                        startActivity(intenttransf)

//                            setAds();
                    }

                    "Video Lectures" -> if (mInterstitialAd != null) {
                        mInterstitialAd!!.show(this@HomeActivity)
                        mInterstitialAd!!.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    intenttransf =
                                        Intent(applicationContext, MainActivity::class.java)
                                    Toast.makeText(
                                        applicationContext,
                                        "After clicking on \"View\" button scroll up ⬆",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    eurl = "https://econtent.msbte.ac.in/econtent/econtent_home.php"
                                    intenttransf!!.putExtra("url", eurl)
                                    startActivity(intenttransf)
                                    mInterstitialAd = null
                                    //                                    setAds();
                                }
                            }
                    } else {
                        intenttransf = Intent(applicationContext, MainActivity::class.java)
                        Toast.makeText(
                            applicationContext,
                            "After clicking on \"View\" button scroll up ⬆",
                            Toast.LENGTH_LONG
                        ).show()
                        eurl = "https://econtent.msbte.ac.in/econtent/econtent_home.php"
                        intenttransf!!.putExtra("url", eurl)
                        startActivity(intenttransf)

//                            setAds();
                    }
                }
            }
        })


//        msquestion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (mInterstitialAd != null) {
//
//                    mInterstitialAd.show(HomeActivity.this);
//
//                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            super.onAdDismissedFullScreenContent();
//
//                            intenttransf = new Intent(getApplicationContext(), MainActivity.class);
//                            Toast.makeText(getApplicationContext(), "After clicking on \"View\" button scroll up ⬆", Toast.LENGTH_LONG).show();
//                            eurl = "https://econtent.msbte.ac.in/econtent/econtent_home.php";
//                            intenttransf.putExtra("url", eurl);
//                            startActivity(intenttransf);
//
//                            mInterstitialAd = null;
//                            setAds();
//
//                        }
//                    });
//
//                } else {
//
//                    intenttransf = new Intent(getApplicationContext(), MainActivity.class);
//                    Toast.makeText(getApplicationContext(), "After clicking on \"View\" button scroll up ⬆", Toast.LENGTH_LONG).show();
//                    eurl = "https://econtent.msbte.ac.in/econtent/econtent_home.php";
//                    intenttransf.putExtra("url", eurl);
//                    startActivity(intenttransf);
//
//                    setAds();
//
//                }
//
//            }
//        });
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.barmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sharebtn -> try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, " " + getString(R.string.app_name))
                val msg =
                    """Hey, I found a nice app Maha360 App. This is the best study resource app for SPPU and MU students,

 It have Question papers, Syllabus, Notes and many more Download now 
 https://play.google.com/store/apps/details?id=com.sppu.questionpaper 

""" // Change your message
                intent.putExtra(Intent.EXTRA_TEXT, msg)
                startActivity(Intent.createChooser(intent, "Share App with your friends"))
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show()
            }

            R.id.telegrambtn -> try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://t.me/+1Zls6H6YehxhZTU9")
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show()
            }

            R.id.dev1 -> try {
                val intent = Intent(this, About_Activity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setAds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.intrestial_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    mInterstitialAd = null
                }
            })
    }

    override fun onBackPressed() { //double press to exit
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast!!.cancel()
            super.onBackPressed()
            return
        } else {
            backToast = Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT)
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
    }

//    override fun onInAppUpdateError(code: Int, error: Throwable) {}
//    override fun onInAppUpdateStatus(status: InAppUpdateStatus) {
//        if (status.isDownloaded) {
//            val view = window.decorView.findViewById<View>(android.R.id.content)
//            val snackbar = Snackbar.make(
//                view,
//                "App has been downloaded successfullly",
//                Snackbar.LENGTH_INDEFINITE
//            )
//            snackbar.setAction("") { inAppUpdateManager!!.completeUpdate() }
//            snackbar.show()
//        }
//    } //    public void onClickStart(View view) {

    //        startActivity(new Intent(this, NewFeatureActivity.class));
    //    }
    companion object {
        val MOBILE_OS = arrayOf(
            "MSBTE Content",
            "Projects",
            "Question Paper",
            "Model Answer",
            "MSBTE Solution Pro",
            "Video Lectures"
        )
    }
}