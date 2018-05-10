package com.example.deepika.travelguide.activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.deepika.travelguide.R;


public class MainActivity extends AppCompatActivity {


    Button signup_btn, login_btn;
    Fragment fr;
    FragmentManager fm;
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView1 = (TextView)findViewById(R.id.textView);
        TextView textView2 = (TextView)findViewById(R.id.tagText);

        Typeface font = Typeface.createFromAsset(getAssets(),"font/irmatextroundstdbold.otf");
        textView1.setTypeface(font);
        textView2.setTypeface(font);

        signup_btn = (Button)findViewById(R.id.signupBTN);
        login_btn = (Button)findViewById(R.id.loginBTN);

        signup_btn.setTypeface(font);
        login_btn.setTypeface(font);


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr = new LoginFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                ft.add(R.id.fragment_container, fr);
                ft.addToBackStack(null);
                ft.commit();


            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fr = new signupFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                ft.add(R.id.fragment_container, fr);
                ft.addToBackStack(null);
                ft.commit();


            }
        });

    }
}
