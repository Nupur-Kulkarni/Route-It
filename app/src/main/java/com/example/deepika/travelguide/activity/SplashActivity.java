package com.example.deepika.travelguide.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.deepika.travelguide.R;
import com.google.android.gms.common.api.GoogleApiClient;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    int i=0;
    Context context;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        TextView textView1 = (TextView)findViewById(R.id.splash);
        Typeface font = Typeface.createFromAsset(getAssets(),"font/irmatextroundstdbold.otf");
        textView1.setTypeface(font);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);

    }

}
