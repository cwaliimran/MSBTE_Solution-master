package com.msbte.modelanswerpaper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.legacy.widget.Space;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;




public class SplashScreen extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        preferences = this.getSharedPreferences("Splash",MODE_PRIVATE);
        editor = preferences.edit();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
            }
        },4000);
    }
}