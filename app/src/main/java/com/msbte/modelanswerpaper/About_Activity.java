package com.msbte.modelanswerpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

public class About_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);



        AboutView aboutView = AboutBuilder.with(this)
                .setPhoto(R.drawable.onkar)
//                .setCover(R.drawable.f)
                .setName("Onkar Dokhe")
                .setSubTitle("DevOps Engineer")
                .setBrief("I'm a passionate DevOps and cloud enthusiast, dedicated to learning and innovating in the world of mobile app development, with expertise in Flutter and native application development.")
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .addGitHubLink("onkar-dokhe")
                .addInstagramLink("onkar_dokhe")
                .addLinkedInLink("onkar-dokhe")
                .addTwitterLink("onkar_dokhe")
                .addYoutubeChannelLink("UCUsJ7TEk5rjzGrJG3eyH8MA")
                .addWebsiteLink("https://bio.link/onkar_dokhe")
                .addEmailLink("phonixdev007@gmail.com")
                .addGooglePlayStoreLink("5736742487249562519")
                .addFiveStarsAction()
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .build();

        addContentView(aboutView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }
}