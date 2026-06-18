package com.example.luminastay.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luminastay.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is logged in
                SharedPreferences prefs = getSharedPreferences("LuminaStayPrefs", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

                Intent intent;
                if (isLoggedIn) {
                    // Go to Room List if already logged in
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    // Go to Login if not logged in
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}