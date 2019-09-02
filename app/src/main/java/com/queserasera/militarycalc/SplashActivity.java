package com.queserasera.militarycalc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    Handler handler = new Handler();
    private SharedPreferences appData;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                if (appData.getBoolean("INPUT", false)) showMain();
                else showInput();
            }
        }, 1000);
    }
    public void showMain(){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, R.anim.fadeout);
    }
    public void showInput(){
        Intent intent = new Intent(getBaseContext(), InputActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, R.anim.fadeout);
    }
}
