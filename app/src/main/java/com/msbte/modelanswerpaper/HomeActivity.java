package com.msbte.modelanswerpaper;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;


public class HomeActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {


    GridView gridView;
    Intent intenttransf;

    static final String[] MOBILE_OS = new String[]{
            "MSBTE Content", "Projects", "Question Paper", "Model Answer", "MSBTE Solution Pro", "Video Lectures"};

    InAppUpdateManager inAppUpdateManager;
    private long backPressedTime;
    private Toast backToast;
    private String eurl;


    private Toolbar toolbar;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ImageSlider imageSlider = findViewById(R.id.image_slider);

        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/msbte-solution.appspot.com/o/slider%2F1.png?alt=media&token=b8a3e1f5-74e4-4485-9d9b-6fbfd02714d8", ScaleTypes.FIT));
        /* ad1 */
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/msbte-solution.appspot.com/o/slider%2F2.png?alt=media&token=c85167af-1fa9-48a3-8add-876df8f22efc", ScaleTypes.FIT));
        /* ad2 */
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/msbte-solution.appspot.com/o/slider%2F4.png?alt=media&token=689f16dd-b6e9-4efa-a9cc-116da0bbce83", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/msbte-solution.appspot.com/o/slider%2F5.png?alt=media&token=73d1e962-960c-4c80-84df-8282a14f527f", ScaleTypes.FIT));

        imageSlider.setImageList(slideModels);

        setAds();

        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                Log.d(TAG, "Selected item " + i);
                switch (i) {
                    case 0:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/3Tf1ZQe")));
                        break;
                    case 1:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/3RLmfrm")));
                        break;
                    case 2:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/3ekpisP")));
                        break;
                    case 3:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/3CPRgq4")));
                        break;
                }
            }
        });

        inAppUpdateManager = InAppUpdateManager.Builder(this, 101)
                .resumeUpdates(true)
                .mode(Constants.UpdateMode.IMMEDIATE)   // Immdiate Means Forcefully Install And Felexiable means no thanks option
                .snackBarAction("An update has just been downloaded")
                .snackBarAction("RESTART")
                .handler(this);
        inAppUpdateManager.checkForAppUpdate();

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(new ImageAdapter(this, MOBILE_OS));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String name = String.valueOf(((TextView) v.findViewById(R.id.grid_item_label)).getText());

                switch (name) {

                    case "Model Answer":
                        if (mInterstitialAd != null) {

                            mInterstitialAd.show(HomeActivity.this);

                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();

                                    intenttransf = new Intent(getApplicationContext(), MainActivity.class);
                                    eurl = "https://drive.google.com/drive/folders/1t_lRCTROxXT8XdplqgIf64c31f1vqXoD?usp=sharing";
                                    intenttransf.putExtra("url", eurl);
                                    startActivity(intenttransf);

                                    mInterstitialAd = null;
//                                    setAds();

                                }
                            });

                        } else {

                            intenttransf = new Intent(getApplicationContext(), MainActivity.class);
                            eurl = "https://drive.google.com/drive/folders/1t_lRCTROxXT8XdplqgIf64c31f1vqXoD?usp=sharing";
                            intenttransf.putExtra("url", eurl);
                            startActivity(intenttransf);
//                            setAds();

                        }

                        break;

                    case "Projects":
                        if (mInterstitialAd != null) {

                            mInterstitialAd.show(HomeActivity.this);

                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();

                                    intenttransf = new Intent(getApplicationContext(), microproject_activity.class);
                                    startActivity(intenttransf);

                                    mInterstitialAd = null;
//                                    setAds();

                                }
                            });

                        } else {

                            intenttransf = new Intent(getApplicationContext(), microproject_activity.class);
                            startActivity(intenttransf);
//                            setAds();

                        }

                        break;

                    case "MSBTE Content":
                    case "Question Paper":
                        if (mInterstitialAd != null) {

                            mInterstitialAd.show(HomeActivity.this);

                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();

                                    intenttransf = new Intent(getApplicationContext(), NewFeatureActivity.class);
                                    startActivity(intenttransf);

                                    mInterstitialAd = null;
//                                    setAds();

                                }
                            });

                        } else {

                            intenttransf = new Intent(getApplicationContext(), NewFeatureActivity.class);
                            startActivity(intenttransf);
//                            setAds();

                        }

                        break;

                    case "MSBTE Solution Pro":
                        if (mInterstitialAd != null) {

                            mInterstitialAd.show(HomeActivity.this);

                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();

                                    intenttransf = new Intent(getApplicationContext(), DumpsActivity.class);
                                    startActivity(intenttransf);

                                    mInterstitialAd = null;
//                                    setAds();

                                }
                            });

                        } else {

                            intenttransf = new Intent(getApplicationContext(), DumpsActivity.class);
                            startActivity(intenttransf);

//                            setAds();

                        }
                        break;

                    case "Video Lectures":
                        if (mInterstitialAd != null) {

                            mInterstitialAd.show(HomeActivity.this);

                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();

                                    intenttransf = new Intent(getApplicationContext(), MainActivity.class);
                                    Toast.makeText(getApplicationContext(), "After clicking on \"View\" button scroll up ⬆", Toast.LENGTH_LONG).show();
                                    eurl = "https://econtent.msbte.ac.in/econtent/econtent_home.php";
                                    intenttransf.putExtra("url", eurl);
                                    startActivity(intenttransf);

                                    mInterstitialAd = null;
//                                    setAds();

                                }
                            });

                        } else {

                            intenttransf = new Intent(getApplicationContext(), MainActivity.class);
                            Toast.makeText(getApplicationContext(), "After clicking on \"View\" button scroll up ⬆", Toast.LENGTH_LONG).show();
                            eurl = "https://econtent.msbte.ac.in/econtent/econtent_home.php";
                            intenttransf.putExtra("url", eurl);
                            startActivity(intenttransf);

//                            setAds();

                        }

                        break;

                }
            }
        });


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.barmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sharebtn:
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, " " + getString(R.string.app_name));
                    String msg = "Hey, I found a nice app Maha360 App. This is the best study resource app for SPPU and MU students,\n\n It have Question papers, Syllabus, Notes and many more Download now \n https://play.google.com/store/apps/details?id=com.sppu.questionpaper " + "\n\n"; // Change your message
                    intent.putExtra(Intent.EXTRA_TEXT, msg);
                    startActivity(Intent.createChooser(intent, "Share App with your friends"));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.telegrambtn:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+1Zls6H6YehxhZTU9")));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dev1:
                try {
                    Intent intent = new Intent(this, About_Activity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    public void setAds() {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.intrestial_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });


    }


    @Override
    public void onBackPressed() { //double press to exit
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

    @Override
    public void onInAppUpdateError(int code, Throwable error) {

    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {

        if (status.isDownloaded()) {
            View view = getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(view,
                    "App has been downloaded successfullly",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inAppUpdateManager.completeUpdate();

                }
            });
            snackbar.show();
        }

    }

//    public void onClickStart(View view) {
//        startActivity(new Intent(this, NewFeatureActivity.class));
//    }
}