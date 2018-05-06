package com.example.deepika.travelguide.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.deepika.travelguide.R;

public class SelectCity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "font/irmatextroundstdmedium.otf");
        TextView tv = (TextView) findViewById(R.id.Selectcity_txt);
        tv.setTypeface(tf);

        ((ImageButton) findViewById(R.id.Baltimore)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.NewYork)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.WashingtonDC)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.LasVegas)).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getApplicationContext(),StartLocation.class);

            switch (v. getId()) {
                case R.id.Baltimore:
                    intent.putExtra("City_latlon","Baltimore");
                    startActivity(intent);
                    break;
                case R.id.WashingtonDC:
                    intent.putExtra("City_latlon","WashingtonDC");
                    startActivity(intent);
                    break;
                case R.id.NewYork:
                    intent.putExtra("City_latlon","NewYork");
                    startActivity(intent);
                    break;
                case R.id.LasVegas:
                    intent.putExtra("City_latlon","LasVegas");
                    startActivity(intent);
                    break;

            }

    }
}
